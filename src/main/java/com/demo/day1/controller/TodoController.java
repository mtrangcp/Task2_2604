package com.demo.day1.controller;

import com.demo.day1.model.entity.Todo;
import com.demo.day1.repository.TodoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class TodoController {
    @Autowired
    private TodoRepository todoRepository;

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("todos", todoRepository.findAll());
        return "index";
    }

    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("todo", new Todo());
        return "add";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model,  RedirectAttributes redirectAttributes) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isEmpty()) {
            redirectAttributes.addFlashAttribute("mess", "Không tìm thấy Todo!");
            return "redirect:/";
        }

        model.addAttribute("todo", optionalTodo.get());
        return "add";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("todo") Todo todo,
                      BindingResult result,  Model model) {
        if (result.hasErrors()) {
            model.addAttribute("todo", todo);
            return "add";
        }

        todoRepository.save(todo);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        todoRepository.deleteById(id);
        return "redirect:/";
    }
}
