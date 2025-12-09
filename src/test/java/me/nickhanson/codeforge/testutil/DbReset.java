package me.nickhanson.codeforge.testutil;

import me.nickhanson.codeforge.persistence.GenericDao;
import org.junit.jupiter.api.BeforeEach;


/**
 * Abstract class to reset database before each test
 */
public abstract class DbReset {
    @BeforeEach
    public void setUp() {
        try {
            TestDbCleaner.purgeCoreTables();
        } catch (Exception e) {
            // Fallback to full SQL reset if purge fails
            Database database = Database.getInstance();
            database.runSQL("cleandb.sql");
        }
    }

    /**
     * Helper method to create a GenericDao for a given type
     *
     * @param type the class type for the DAO
     * @param <T>  the type parameter
     * @return a GenericDao for the specified type
     */
    protected <T> GenericDao<T> dao(Class<T> type) {
        return new GenericDao<>(type);
    }
}
