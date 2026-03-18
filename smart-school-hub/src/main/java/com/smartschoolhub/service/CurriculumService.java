package com.smartschoolhub.service;

import com.smartschoolhub.domain.Curriculum;
import com.smartschoolhub.domain.SchoolClass;
import com.smartschoolhub.domain.Subject;
import com.smartschoolhub.repository.CurriculumRepository;
import com.smartschoolhub.repository.SchoolClassRepository;
import com.smartschoolhub.repository.SubjectRepository;
import com.smartschoolhub.service.dto.CurriculumRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CurriculumService {
    private final CurriculumRepository curriculumRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final SubjectRepository subjectRepository;

    public CurriculumService(CurriculumRepository curriculumRepository,
                             SchoolClassRepository schoolClassRepository,
                             SubjectRepository subjectRepository) {
        this.curriculumRepository = curriculumRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.subjectRepository = subjectRepository;
    }

    public List<Curriculum> getCurriculumByClass(Long classId) {
        SchoolClass schoolClass = schoolClassRepository.findById(classId)
            .orElseThrow(() -> new ResourceNotFoundException("Class not found: " + classId));
        return curriculumRepository.findBySchoolClass(schoolClass);
    }

    @Transactional
    public Curriculum createOrUpdateCurriculum(CurriculumRequest request) {
        SchoolClass schoolClass = schoolClassRepository.findById(request.getSchoolClassId())
            .orElseThrow(() -> new ResourceNotFoundException("Class not found: " + request.getSchoolClassId()));

        Subject subject = subjectRepository.findById(request.getSubjectId())
            .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + request.getSubjectId()));

        Curriculum curriculum = curriculumRepository.findBySchoolClassAndSubject(schoolClass, subject)
                .orElse(new Curriculum());

        curriculum.setSchoolClass(schoolClass);
        curriculum.setSubject(subject);
        curriculum.setPeriodsPerWeek(request.getPeriodsPerWeek());

        return curriculumRepository.save(curriculum);
    }

    @Transactional
    public void deleteCurriculum(Long id) {
        if (!curriculumRepository.existsById(id)) {
            throw new ResourceNotFoundException("Curriculum entry not found: " + id);
        }
        curriculumRepository.deleteById(id);
    }
}
