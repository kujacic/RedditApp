package igor.osa.reddit.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import igor.osa.reddit.be.model.Report;

public interface ReportRepository extends JpaRepository<Report, Integer>{

}
