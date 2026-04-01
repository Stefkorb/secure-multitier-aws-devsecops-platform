package gr.hua.dit.kvdb.kvdb.domain.request;

public enum RequestStatus {
    SUBMITTED,              // Υποβλήθηκε
    RECEIVED,               // Σε παραλαβή
    IN_PROGRESS,            // Σε επεξεργασία
    WAITING_FOR_INFO,       // Σε αναμονή επιπλέον στοιχείων
    COMPLETED,              // Ολοκληρωμένο
    REJECTED                // Απορρίφθηκε
}
