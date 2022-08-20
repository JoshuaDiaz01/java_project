package com.javaproject.aaj.weekendwalkers.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.javaproject.aaj.weekendwalkers.models.Event;
import com.javaproject.aaj.weekendwalkers.models.User;
import com.javaproject.aaj.weekendwalkers.services.EventService;
import com.javaproject.aaj.weekendwalkers.services.UserService;

@Controller
@RequestMapping("/events")
public class EventController {
	@Autowired
	private EventService eventService;
	@Autowired
	private UserService userService;

//	CREATE
	@GetMapping("/new")
	public String newEvent(Model model, @ModelAttribute("event") Event event, HttpSession session) {
		if (session.getAttribute("userId") != null) {
			User user = userService.getOne((Long) session.getAttribute("userId"));
			model.addAttribute("user", user);
		}
		return "create_event.jsp";
	}

	@PostMapping("/create")
	public String createEvent(HttpSession session, Model model, @Valid @ModelAttribute("event") Event event,
			BindingResult result) {
		if (result.hasErrors()) {
			User user = userService.getOne((Long) session.getAttribute("userId"));
			model.addAttribute("user", user);
			return "create_event.jsp";
		}
		Event newEvent = eventService.save(event);
		return "redirect:/events/" + newEvent.getId();
	}

//	READ

	@GetMapping("")
	public String showAllEvents(Model model, HttpSession session) {
		if (session.getAttribute("userId") != null) {
			User user = userService.getOne((Long) session.getAttribute("userId"));
			model.addAttribute("user", user);
		}
		List<Event> listOfEvents = eventService.getAll();
		model.addAttribute("listOfEvents", listOfEvents);
		return "dashboard.jsp";
	}

	@GetMapping("/{id}")
	public String showOneEvent(@PathVariable("id") Long id, Model model, HttpSession session) {
		User user = userService.getOne((Long) session.getAttribute("userId"));
		model.addAttribute("user", user);

		Event event = eventService.getOne(id);
		model.addAttribute("event", event);

		return "one_event.jsp";
	}

//	UPDATE

	@GetMapping("/{id}/edit")
	public String editEvent(@PathVariable("id") Long id, Model model, HttpSession session) {
		User user = userService.getOne((Long) session.getAttribute("userId"));
		model.addAttribute("user", user);

		Event event = eventService.getOne(id);
		model.addAttribute("event", event);

		return "edit_event.jsp";
	}

	@PostMapping("/{id}/update")
	public String updateEvent(@PathVariable("id") Long id, HttpSession session, Model model,
			@Valid @ModelAttribute("event") Event event, BindingResult result) {
		if (result.hasErrors()) {
			User user = userService.getOne((Long) session.getAttribute("userId"));
			model.addAttribute("user", user);
			return "create_event.jsp";
		}
		Event editedEvent = eventService.save(event);
		return "redirect:/events/" + editedEvent.getId();
	}

//	DELETE
	@DeleteMapping("/{id}/delete")
	public String deleteEvent(@PathVariable("id") Long id) {
		eventService.delete(id);
		return "redirect:/events";
	}
}
