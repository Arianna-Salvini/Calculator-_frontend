package it.reply.repository;

import it.reply.data.CalculatorRecord;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class HistoryRepository {

    private static final String GET_RECORDS = "SELECT * FROM calculator_record ORDER BY id";
    private static final String ADD_RECORDS = "INSERT INTO calculator_record (numerator, denominator, operator, result, error, timestamp ) VALUES (:numerator, :denominator, :operator, :result, :error, :timestamp ) ";
    private static final String DELETE_RECORDS_BY_ID = "DELETE FROM calculator_record WHERE id= :id ";

    @Inject
    EntityManager em;

    public List<CalculatorRecord> getUsers() {
        Query q = em.createNativeQuery(GET_RECORDS, Tuple.class);

        List<Tuple> result = (List<Tuple>) q.getResultList();
        return result.stream().map(this::toRecordData).collect(Collectors.toList());
    }

    public void addRecord(double numerator, double denominator, char operator, String result, String error) {

        Query q = em.createNamedQuery(ADD_RECORDS);
        q.setParameter("numerator", numerator);
        q.setParameter("denominator", denominator);
        q.setParameter("operator", operator);
        q.setParameter("result", result);
        q.setParameter("error", error);

        int updateRecord = q.executeUpdate();
        if (updateRecord != 1) {
            throw new RuntimeException("addUser error");
        }
    }

    public void deleteUserById(int id) {
        Query q = em.createNativeQuery(DELETE_RECORDS_BY_ID);
        q.setParameter("id", id);

        int result = q.executeUpdate();
        if (result != 1) {
            throw new RuntimeException("deleteUserById error");
        }
    }

    private CalculatorRecord toRecordData(Tuple tuple) {
        CalculatorRecord history = new CalculatorRecord();
        history.setId(tuple.get("id", Long.class));
        history.setNumerator(tuple.get("numerator", Double.class));
        history.setDenominator(tuple.get("denominator", Double.class));
        history.setOperator(tuple.get("operator", Character.class));
        history.setResult(tuple.get("result", String.class));
        history.setError(tuple.get("error", String.class));
        history.setTimestamp(tuple.get("timestamp", LocalDateTime.class));
        return history;
    }
}
