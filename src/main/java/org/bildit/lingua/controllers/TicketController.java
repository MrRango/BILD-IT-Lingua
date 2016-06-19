package org.bildit.lingua.controllers;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.bildit.lingua.model.Ticket;
import org.bildit.lingua.service.TicketService;
import org.bildit.lingua.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TicketController {
	
	private static final String HOME = "home";
	private static final String TICKETS = "tickets";
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * @author Bojan Aleksic
	 * @param principal
	 * @param model
	 * @param urlRequest
	 * @param page
	 * @param pageable
	 * @return
	 * Method receives all tickets by current user, determines which category by URL request,
	 * and sends back data within the model
	 */
	@RequestMapping("/fragments/get-tickets.html")
	@ResponseBody
	public ModelAndView getAllTickets(
			Principal principal, 
			ModelAndView model, 
			@RequestParam("urlData") String urlRequest, 
			@RequestParam(value="page", required=false) int page, 
			@PageableDefault(value=2) Pageable pageable) {
		
		if("ticket-all".equals(urlRequest)) {
			Page<Ticket> allTickets = ticketService.getAllTicketsByUsername(principal.getName(), pageable);
			model.addObject(TICKETS, allTickets);
			model.addObject("totalPages", allTickets.getTotalPages());
		} else if("ticket-active".equals(urlRequest)) {
			model.addObject(TICKETS, ticketService.getAllActiveTicketsByUsername(principal.getName()));
		} else if("ticket-deleted".equals(urlRequest)) {
			model.addObject(TICKETS, ticketService.getAllDeactivatedTicketsByUsername(principal.getName()));
		} else if("ticket-moderated".equals(urlRequest)) {
			model.addObject(TICKETS, ticketService.getAllModeratedTicketsByUsername(principal.getName()));
		}
		return model;
	}
	
	/**
	 * @author Bojan Aleksic
	 * @param languageTitle
	 * @param principal
	 * @return
	 * Updating user's foreign (learning) language
	 */
	@RequestMapping(value="/set-foreign-language", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public Map<String, String> setForeignLanguage(@RequestBody String languageTitle, Principal principal) {
		Map<String, String> map = new HashMap<>();
		userService.setForeignLanguageForUser(principal.getName(), languageTitle);
		map.put("languageTitle", languageTitle);
		return map;
	}
	
	/**
	 * @author Bojan Aleksic
	 * @param ticket
	 * @param principal
	 * @return
	 */
	@RequestMapping("/create-ticket")
	public String createNewTicket(@ModelAttribute("ticket") Ticket ticket, Principal principal) {
		ticketService.saveTicket(ticket, principal.getName());
		return HOME;
	}
	
	/**
	 * @author Bojan Aleksic
	 * @param ticket
	 * @return
	 */
	@RequestMapping("/edit-ticket")
	public String editTicket(@ModelAttribute("ticket") Ticket ticket) {
		ticketService.updateTicket(ticket.getTextDomestic(), ticket.getTextForeign(), ticket.getCategory(), ticket.getId());
		return HOME;
	}
	
	/**
	 * @author Mladen Todorovic
	 * Method: add like to ticket by ticket-id and user's username
	 */
	@RequestMapping("/add-like")
	@ResponseBody
	public String addLike(@RequestParam("id") String ticketId, Principal principal) {
		Long id = Long.parseLong(ticketId);
		return ticketService.addLikeToTicket(id, principal.getName());
	}
	
	/**
	 * @author Mladen Todorovic
	 * Method: add like to ticket by ticket-id and user's username
	 */
	@RequestMapping("/add-dislike")
	@ResponseBody
	public String addDislike(@RequestParam("id") String ticketId, Principal principal) {
		Long id = Long.parseLong(ticketId);
		return ticketService.addDislikeToTicket(id, principal.getName());
	}
	
	/**
	 * @author Mladen Todorovic
	 * Method: delete ticket by ticket-id and user's username
	 */
	@RequestMapping("/delete-ticket")
	public String deleteTicket(@ModelAttribute Ticket ticket, @RequestParam("id") Long id, Principal principal) {
		ticketService.deleteTicket(id, principal.getName());
		return HOME;
	}
}