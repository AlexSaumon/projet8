
package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RatingController {

    private final RatingRepository ratingRepository;

    public RatingController(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @GetMapping("/rating/list")
    public String home(Model model) {
        model.addAttribute("ratings", ratingRepository.findAll());
        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm(Model model) {
        model.addAttribute("rating", new Rating());
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(
            @Valid @ModelAttribute("rating") Rating rating,
            BindingResult result) {

        if (result.hasErrors()) {
            return "rating/add";
        }

        ratingRepository.save(rating);
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(
            @PathVariable("id") Integer id,
            Model model) {

        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid rating Id: " + id));

        model.addAttribute("rating", rating);
        return "rating/update";
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("rating") Rating rating,
            BindingResult result) {

        if (result.hasErrors()) {
            return "rating/update";
        }

        if (!ratingRepository.existsById(id)) {
            return "redirect:/rating/list";
        }

        rating.setId(id);
        ratingRepository.save(rating);

        return "redirect:/rating/list";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id) {

        if (ratingRepository.existsById(id)) {
            ratingRepository.deleteById(id);
        }

        return "redirect:/rating/list";
    }
}
