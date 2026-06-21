document.documentElement.classList.add("motion-ready");

document.addEventListener("DOMContentLoaded", () => {
    const revealItems = document.querySelectorAll(".reveal, .reveal-group > *");

    if ("IntersectionObserver" in window) {
        const observer = new IntersectionObserver((entries) => {
            entries.forEach((entry) => {
                if (entry.isIntersecting) {
                    entry.target.classList.add("is-visible");
                    observer.unobserve(entry.target);
                }
            });
        }, { threshold: 0.12 });

        revealItems.forEach((item, index) => {
            item.style.setProperty("--delay", `${Math.min(index * 70, 420)}ms`);
            observer.observe(item);
        });
    } else {
        revealItems.forEach((item) => item.classList.add("is-visible"));
    }

    document.querySelectorAll(".item-card").forEach((card) => {
        card.addEventListener("pointermove", (event) => {
            const rect = card.getBoundingClientRect();
            const x = ((event.clientX - rect.left) / rect.width - 0.5) * 8;
            const y = ((event.clientY - rect.top) / rect.height - 0.5) * -8;
            card.style.setProperty("--tilt-x", `${y}deg`);
            card.style.setProperty("--tilt-y", `${x}deg`);
        });

        card.addEventListener("pointerleave", () => {
            card.style.setProperty("--tilt-x", "0deg");
            card.style.setProperty("--tilt-y", "0deg");
        });
    });
});
