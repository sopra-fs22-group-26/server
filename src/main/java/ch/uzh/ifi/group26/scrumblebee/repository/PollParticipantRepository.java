package ch.uzh.ifi.group26.scrumblebee.repository;

import ch.uzh.ifi.group26.scrumblebee.entity.PollParticipant;
import ch.uzh.ifi.group26.scrumblebee.entity.PollParticipantKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("PollParticipantRepository")
public interface PollParticipantRepository extends JpaRepository<PollParticipant, PollParticipantKey> {
}
