package by.tryput.moneyman.repository.custom.impl;

import by.tryput.moneyman.domain.Status;
import by.tryput.moneyman.repository.custom.ExecutedPipelineCustom;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


public class ExecutedPipelineRepositoryImpl implements ExecutedPipelineCustom {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Override
    public Status getStatus(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager
                .createQuery("SELECT ex.status from ExecutedPipeline ex where ex.id = :id", Status.class)
                .setParameter("id", id).getSingleResult();
    }

    @Override
    public void setStatus(Long id, Status status) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager
                .createQuery("UPDATE  ExecutedPipeline ex set ex.status = :status where ex.id = :id")
                .setParameter("status", status)
                .setParameter("id", id);
    }
}
