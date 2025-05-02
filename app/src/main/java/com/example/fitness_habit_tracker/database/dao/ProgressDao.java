@Dao
public interface ProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProgress(Progress progress);

    @Query("SELECT * FROM progress WHERE habitId = :habitId AND date = :date LIMIT 1")
    Progress getProgressByDate(int habitId, String date);
}
