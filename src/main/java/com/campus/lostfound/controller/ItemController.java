package com.campus.lostfound.controller;

import com.campus.lostfound.model.ItemStatus;
import com.campus.lostfound.model.LostItem;
import com.campus.lostfound.repository.LostItemRepository;
import com.campus.lostfound.repository.UserRepository;
import com.campus.lostfound.service.FileStorageService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/items")
public class ItemController {
    private final LostItemRepository itemRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public ItemController(
            LostItemRepository itemRepository,
            UserRepository userRepository,
            FileStorageService fileStorageService) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        var query = keyword == null ? "" : keyword.trim();
        var items = query.isBlank()
                ? itemRepository.findAllByOrderByCreatedAtDesc()
                : itemRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrLocationContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrderByCreatedAtDesc(
                        query, query, query, query);
        model.addAttribute("items", items);
        model.addAttribute("keyword", query);
        model.addAttribute("totalCount", items.size());
        model.addAttribute("lostCount", items.stream().filter(item -> item.getStatus() == ItemStatus.LOST).count());
        model.addAttribute("foundCount", items.stream().filter(item -> item.getStatus() == ItemStatus.FOUND).count());
        model.addAttribute("returnedCount", items.stream().filter(item -> item.getStatus() == ItemStatus.RETURNED).count());
        return "items/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, Principal principal) {
        var item = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("item", item);
        model.addAttribute("canEdit", ownsItem(item, principal));
        return "items/detail";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("item", new LostItem());
        model.addAttribute("statuses", ItemStatus.values());
        return "items/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("item") LostItem item,
            BindingResult result,
            @RequestParam("image") MultipartFile image,
            Principal principal,
            Model model,
            RedirectAttributes redirectAttributes) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("statuses", ItemStatus.values());
            return "items/form";
        }

        item.setOwner(currentUser(principal));
        item.setImagePath(fileStorageService.store(image));
        itemRepository.save(item);
        redirectAttributes.addFlashAttribute("success", "Item post created.");
        return "redirect:/items/" + item.getId();
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Principal principal, Model model) {
        var item = requireOwnedItem(id, principal);
        model.addAttribute("item", item);
        model.addAttribute("statuses", ItemStatus.values());
        return "items/form";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("item") LostItem form,
            BindingResult result,
            @RequestParam("image") MultipartFile image,
            Principal principal,
            Model model,
            RedirectAttributes redirectAttributes) throws IOException {
        var item = requireOwnedItem(id, principal);
        if (result.hasErrors()) {
            form.setId(id);
            form.setImagePath(item.getImagePath());
            model.addAttribute("statuses", ItemStatus.values());
            return "items/form";
        }

        item.setTitle(form.getTitle());
        item.setCategory(form.getCategory());
        item.setLocation(form.getLocation());
        item.setDescription(form.getDescription());
        item.setStatus(form.getStatus());
        var newImagePath = fileStorageService.store(image);
        if (newImagePath != null) {
            item.setImagePath(newImagePath);
        }
        itemRepository.save(item);
        redirectAttributes.addFlashAttribute("success", "Item post updated.");
        return "redirect:/items/" + item.getId();
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        var item = requireOwnedItem(id, principal);
        itemRepository.delete(item);
        redirectAttributes.addFlashAttribute("success", "Item post deleted.");
        return "redirect:/items";
    }

    private com.campus.lostfound.model.User currentUser(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }

    private LostItem requireOwnedItem(Long id, Principal principal) {
        var item = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!ownsItem(item, principal)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return item;
    }

    private boolean ownsItem(LostItem item, Principal principal) {
        return principal != null && item.getOwner().getEmail().equals(principal.getName());
    }
}
