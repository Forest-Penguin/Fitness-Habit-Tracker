@Dao
public interface HabitDao {
    @Insert
    void insertHabit(Habit habit);

    @Query("SELECT * FROM habits WHERE userId = :userId ORDER BY createdAt DESC")
    LiveData<List<Habit>> getHabitsByUser(int userId);

    @Delete
    void deleteHabit(Habit habit);
}
