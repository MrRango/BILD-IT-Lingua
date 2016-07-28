package org.bildit.lingua.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.bildit.lingua.model.Language;
import org.bildit.lingua.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @interface UserRepository
 * @author Mladen Todorovic
 * */
public interface UserRepository extends BaseRepository <User, Long> {
	
	List<User> findAll();
	
	/** @author Mladen Todorovic */
	User findUserByUsername(String username);
	
	/** @author Novislav Sekulic */
	@Query("SELECT CASE WHEN COUNT(baseUser) > 0 THEN 'true' ELSE 'false' END FROM BaseUser baseUser WHERE baseUser.username = ?1")
	boolean existByUsername(String username);
	
	/**
	 * @author Bojan Aleksic
	 * @param foreignLanguage
	 * @param id
	 */
	@Modifying // This tells spring-data-jpa to execute update
	@Transactional // @Modifying annotation requires @Transactional in order to work
	@Query("update User u set u.foreignLanguage = ?1 where u.id = ?2")
	void updateForeignLanguageForUser(Language foreignLanguage, Long id);

	/**
	 * @author Bojan Aleksic
	 * @param entryBan
	 * @param id
	 */
	@Modifying
	@Transactional
	@Query("update User u set u.addingBan = ?1 where u.id = ?2")
	void updateNewEntryBan(boolean entryBan, Long id);
	
	/**
	 * @author Bojan ALeksic
	 * @param loginBan
	 * @param id
	 */
	@Modifying
	@Transactional
	@Query("update User u set u.loginBan = ?1 where u.id = ?2")
	void updateLoginBan(boolean loginBan, Long id);

	/**
	 * @author Bojan Aleksic
	 * @param votingBan
	 * @param id
	 */
	@Modifying
	@Transactional
	@Query("update User u set u.votingBan = ?1 where u.id = ?2")
	void updateVotingBan(boolean votingBan, Long id);
	
	/** @author Mladen Todorovic of all these methods below */
	@Query("SELECT u FROM User u WHERE u.votingBan = 1 AND u.addingBan = 1 AND u.enabled = 0 ORDER BY firstName ASC")
	List<User> findAllBannedUsers(Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.votingBan = 1 ORDER BY firstName ASC")
	List<User> findAllVotingBanUsers(Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.addingBan = 1 ORDER BY firstName ASC")
	List<User> findAllAddingBanUsers(Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.enabled = 0 ORDER BY firstName ASC")
	List<User> findAllLoginBanUsers(Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.username LIKE CONCAT(?1, '%') ORDER BY firstName ASC")
	List<User> findAllByUsernameOrderByAsc(String username, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.votingBan = 1 AND u.username LIKE CONCAT(?1, '%') ORDER BY firstName ASC")
	List<User> findAllByVotingBanAndUsernameOrderByAsc(String username, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.addingBan = 1 AND u.username LIKE CONCAT(?1, '%') ORDER BY firstName ASC")
	List<User> findAllByAddingBanAndUsernameOrderByAsc(String username, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.enabled = 0 AND u.username LIKE CONCAT(?1, '%') ORDER BY firstName ASC")
	List<User> findAllByLoginBanAndUsernameOrderByAsc(String username, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE "
			+ "(u.votingBan = 1 OR u.addingBan = 1 OR u.enabled = 0) "
			+ "AND u.username LIKE CONCAT(?1, '%') ORDER BY firstName ASC")
	List<User> findByAllBansAndUsernameOrderByAsc(String username, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.firstName LIKE CONCAT(?1, '%') ORDER BY firstName ASC")
	List<User> findAllByFirstNameOrderByAsc(String firstName, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.votingBan = 1 AND u.firstName LIKE CONCAT(?1, '%') ORDER BY firstName ASC")
	List<User> findAllByVotingBanAndFirstNameOrderByAsc(String firstName, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.addingBan = 1 AND u.firstName LIKE CONCAT(?1, '%') ORDER BY firstName ASC")
	List<User> findAllByAddingBanAndFirstNameOrderByAsc(String firstName, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.enabled = 0 AND u.firstName LIKE CONCAT(?1, '%') ORDER BY firstName ASC")
	List<User> findAllByLoginBanAndFirstNameOrderByAsc(String firstName, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE "
			+ "(u.votingBan = 1 OR u.addingBan = 1 OR u.enabled = 0) "
			+ "AND u.firstName LIKE CONCAT(?1, '%') ORDER BY firstName ASC")
	List<User> findByAllBansAndFirstNameOrderByAsc(String firstName, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.lastName LIKE CONCAT(?1, '%') ORDER BY firstName ASC")
	List<User> findAllByLastNameOrderByAsc(String lastName, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.votingBan = 1 AND u.lastName LIKE CONCAT(?1, '%') ORDER BY firstName ASC")
	List<User> findAllByVotingBanAndLastNameOrderByAsc(String lastName, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.addingBan = 1 AND u.lastName LIKE CONCAT(?1, '%') ORDER BY firstName ASC")
	List<User> findAllByAddingBanAndLastNameOrderByAsc(String lastName, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.enabled = 0 AND u.lastName LIKE CONCAT(?1, '%') ORDER BY firstName ASC")
	List<User> findAllByLoginBanAndLastNameOrderByAsc(String lastName, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE "
			+ "(u.votingBan = 1 OR u.addingBan = 1 OR u.enabled = 0) "
			+ "AND u.lastName LIKE CONCAT(?1, '%') ORDER BY firstName ASC")
	List<User> findByAllBansAndLastNameOrderByAsc(String lastName, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.firstName LIKE CONCAT(?1, '%') AND u.lastName LIKE CONCAT(?2, '%') ORDER BY firstName ASC")
	List<User> findAllByFirstNameAndLastNameOrderByAsc(String firstName, String lastName, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.votingBan = 1 AND u.firstName LIKE CONCAT(?1, '%') AND u.lastName LIKE CONCAT(?2, '%') ORDER BY firstName ASC")
	List<User> findAllByVotingBanAndFirstNameAndLastNameOrderByAsc(String firstName, String lastName, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.addingBan = 1 AND u.firstName LIKE CONCAT(?1, '%') AND u.lastName LIKE CONCAT(?2, '%') ORDER BY firstName ASC")
	List<User> findAllByAddingBanAndFirstNameAndLastNameOrderByAsc(String firstName, String lastName, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.enabled = 0 AND u.firstName LIKE CONCAT(?1, '%') AND u.lastName LIKE CONCAT(?2, '%') ORDER BY firstName ASC")
	List<User> findAllByLoginBanAndFirstNameAndLastNameOrderByAsc(String firstName, String lastName, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE "
			+ "(u.votingBan = 1 OR u.addingBan = 1 OR u.enabled = 0) "
			+ "AND u.firstName LIKE CONCAT(?1, '%') AND u.lastName LIKE CONCAT(?2, '%') ORDER BY firstName ASC")
	List<User> findByAllBansAndFirstNameAndLastNameOrderByAsc(String firstName, String lastName, Pageable pageable);
	
}
