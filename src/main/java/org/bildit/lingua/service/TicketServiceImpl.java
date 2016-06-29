package org.bildit.lingua.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.bildit.lingua.model.Ticket;
import org.bildit.lingua.model.User;
import org.bildit.lingua.model.Vote;
import org.bildit.lingua.repository.LanguageRepository;
import org.bildit.lingua.repository.TicketRepository;
import org.bildit.lingua.repository.UserRepository;
import org.bildit.lingua.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService {
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private VoteRepository voteRepository;
	
	@Autowired
	private LanguageRepository languageRepository;
	
	@Override
	public List<Ticket> getAll() {
		return ticketRepository.findAll();
	}

	@Override
	public Ticket getOne(Long id) {
		return ticketRepository.getOne(id);
	}

	@Override
	public void delete(Long id) {
		ticketRepository.delete(id);
	}

	/* 
	 * @author Bojan Aleksic
	 * This method returns all tickets of current user by username, 5 records per page.
	 */
	@Override
	public Page<Ticket> getAllTicketsByUsername(String username, String learningLanguage, Pageable pageable) {
		return ticketRepository.findAllByUserIdAndLearningLanguage(userRepository.findUserByUsername(username).getId(), languageRepository.getOneByLanguageTitle(learningLanguage), pageable);
	}

	/**
	 * @author Mladen Todorovic
	 * @edit Bojan Aleksic
	 * Method: get list of all active user tickets by username
	 * */
	@Override
	public Page<Ticket> getAllActiveTicketsByUsername(String username, String learningLanguage, Pageable pageable) {
		return ticketRepository.findAllByUserIdAndLearningLanguageAndDeactivatedIsNull(userRepository.findUserByUsername(username).getId(), languageRepository.getOneByLanguageTitle(learningLanguage), pageable);
	}
	/**
	 * @author Mladen Todorovic
	 * @edit Bojan Aleksic
	 * Method: get list of all deleted (deactivated) user tickets by username
	 * */
	@Override
	public Page<Ticket> getAllDeactivatedTicketsByUsername(String username, String learningLanguage, Pageable pageable) {
		return ticketRepository.findAllByUserIdAndLearningLanguageAndDeactivatedIsNotNull(userRepository.findUserByUsername(username).getId(), languageRepository.getOneByLanguageTitle(learningLanguage), pageable);
	}
	/**
	 * @author Mladen Todorovic
	 * @edit Bojan Aleksic
	 * Method: get list of all moderated user tickets by username
	 * */
	@Override
	public Page<Ticket> getAllModeratedTicketsByUsername(String username, String learningLanguage, Pageable pageable) {
		return ticketRepository.findAllByUserIdAndLearningLanguageAndEditedTrue(userRepository.findUserByUsername(username).getId(), languageRepository.getOneByLanguageTitle(learningLanguage), pageable);
	}

	/**
	 * @author Bojan Aleksic
	 * @edit Mladen Todorovic
	 * Save ticket into database
	 */
	@Override
	public Ticket saveTicket(Ticket ticket, String username) {
		User user = userRepository.findUserByUsername(username);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm");
		String date = sdf.format(new Date());
		ticket.setUser(user);
		ticket.setDateCreated(date);
		user.getTickets().add(ticket);
		Vote vote = new Vote(0,0);
		vote.setTicket(ticket);
		ticket.setTicketVotes(vote);
		ticket.setLearningLanguage(user.getForeignLanguage());
		return ticketRepository.saveAndFlush(ticket);
	}
	
	/**
	 * @author Mladen Todorovic
	 * Method: add vote-like to ticket by ticket-id and user's username
	 */
	@Override
	public String addLikeToTicket(Long ticketId, String username) {
		
		User user = userRepository.findUserByUsername(username);
		Ticket ticket = ticketRepository.getOne(ticketId);
		Ticket userTicket = ticketRepository.findOneByUserAndId(user, ticket.getId());
		
		if (userTicket == ticket) {
			return "your-own-ticket";
		}
		Vote vote = new Vote();
		vote = ticket.getTicketVotes();
		Set<User> listOfVotedUsers = vote.getVotedUsers();
		
		if (!listOfVotedUsers.contains(user)) {
			listOfVotedUsers.add(user);
			vote.incrementLikes();
			voteRepository.save(vote);
			return String.valueOf(vote.getLikes());
		} else {
			return "already-voted";
		}
	}
	
	/**
	 * @author Mladen Todorovic
	 * Method: add vote-dislike to ticket by ticket-id and user's username
	 */
	@Override
	public String addDislikeToTicket(Long ticketId, String username) {
		
		User user = userRepository.findUserByUsername(username);
		Ticket ticket = ticketRepository.getOne(ticketId);
		Ticket userTicket = ticketRepository.findOneByUserAndId(user, ticket.getId());
		
		if (userTicket == ticket) {
			return "your-own-ticket";
		}
		Vote vote = new Vote();
		vote = ticket.getTicketVotes();
		Set<User> listOfVotedUsers = vote.getVotedUsers();
		
		if (!listOfVotedUsers.contains(user)) {
			listOfVotedUsers.add(user);
			vote.incrementDislikes();
			voteRepository.save(vote);
			return String.valueOf(vote.getDislikes());
		} else {
			return "already-voted";
		}
	}
	
	/**
	 * @author Mladen Todorovic
	 * Method: update ticket's parameters using ticket-id
	 * */
	@Override
	public void updateTicket(String textDomestic, String textForeign, String category, Long ticketId) {
		ticketRepository.update(textDomestic, textForeign, category, ticketId);
	}
	
	/**
	 * @author Mladen Todorovic
	 * Method: delete a ticket and all its relations by ticket-id and user's username
	 * */
	@Override
	public void deleteTicket(Long id, String username) {	
		User user = userRepository.findUserByUsername(username);
		Ticket ticket = ticketRepository.findOneByUserAndId(user, id);
		Vote vote = voteRepository.findVoteByTicket(ticket);
		vote.getVotedUsers().clear();
		voteRepository.delete(vote);
		ticketRepository.delete(ticket);
	}
	
	/** 
	 * @author Bojan Aleksic
	 * Method returns tickets by User and selected category
	 */
	@Override
	public List<Ticket> getTicketsByUserAndCategory(User user, String category) {
		return ticketRepository.findAllByUserAndCategory(user, category);
	}
	
	/**
	 * @author Bojan Aleksic
	 * Method returns all tickets by provided category
	 */
	@Override
	public List<Ticket> getTicketsByCategory(String category) {
		return ticketRepository.findAllByCategory(category);
	}
	
}
