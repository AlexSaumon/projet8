
package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CurveController {

    private final CurvePointRepository curvePointRepository;

    public CurveController(CurvePointRepository curvePointRepository) {
        this.curvePointRepository = curvePointRepository;
    }

    @GetMapping("/curvePoint/list")
    public String home(Model model) {
        model.addAttribute("curvePoints", curvePointRepository.findAll());
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addCurvePointForm(Model model) {
        model.addAttribute("curvePoint", new CurvePoint());
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(
            @Valid @ModelAttribute("curvePoint") CurvePoint curvePoint,
            BindingResult result) {

        if (result.hasErrors()) {
            return "curvePoint/add";
        }

        curvePointRepository.save(curvePoint);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(
            @PathVariable("id") Integer id,
            Model model) {

        CurvePoint curvePoint = curvePointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid CurvePoint Id: " + id));

        model.addAttribute("curvePoint", curvePoint);
        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateCurvePoint(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("curvePoint") CurvePoint curvePoint,
            BindingResult result) {

        if (result.hasErrors()) {
            return "curvePoint/update";
        }

        if (!curvePointRepository.existsById(id)) {
            return "redirect:/curvePoint/list";
        }

        curvePoint.setId(id);
        curvePointRepository.save(curvePoint);

        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id) {

        if (curvePointRepository.existsById(id)) {
            curvePointRepository.deleteById(id);
        }

        return "redirect:/curvePoint/list";
    }
}
