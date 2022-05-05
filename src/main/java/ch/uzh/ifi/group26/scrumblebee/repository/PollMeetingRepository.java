package ch.uzh.ifi.group26.scrumblebee.repository;

import ch.uzh.ifi.group26.scrumblebee.entity.PollMeeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pollMeetingRepository")
public interface PollMeetingRepository extends JpaRepository<PollMeeting, Long> {
    PollMeeting findByMeetingId(long taskId);
}
