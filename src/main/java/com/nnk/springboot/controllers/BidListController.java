
package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class BidListController {

    private final BidListRepository bidListRepository;

    public BidListController(BidListRepository bidListRepository) {
        this.bidListRepository = bidListRepository;
    }

    @GetMapping("/bidList/list")
    public String home(Model model) {
        model.addAttribute("bidLists", bidListRepository.findAll());
        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(Model model) {
        model.addAttribute("bidList", new BidList());
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(
            @Valid @ModelAttribute("bidList") BidList bidList,
            BindingResult result) {

        if (result.hasErrors()) {
            return "bidList/add";
        }

        bidListRepository.save(bidList);
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(
            @PathVariable("id") Integer id,
            Model model) {

        BidList bidList = bidListRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid bid Id: " + id));

        model.addAttribute("bidList", bidList);

        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("bidList") BidList bidList,
            BindingResult result) {

        if (result.hasErrors()) {
            return "bidList/update";
        }

        if (!bidListRepository.existsById(id)) {
            return "redirect:/bidList/list";
        }

        bidList.setBidListId(id);
        bidListRepository.save(bidList);

        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id) {

        if (bidListRepository.existsById(id)) {
            bidListRepository.deleteById(id);
        }

        return "redirect:/bidList/list";
    }
}
