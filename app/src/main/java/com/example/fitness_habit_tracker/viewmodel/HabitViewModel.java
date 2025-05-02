public class HabitViewModel extends AndroidViewModel {
    private final HabitDao habitDao;
    private final ProgressDao progressDao;

    public HabitViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        habitDao = db.habitDao();
        progressDao = db.progressDao();
    }

    public LiveData<List<Habit>> getUserHabits(int userId) {
        return habitDao.getHabitsByUser(userId);
    }

    public void addHabit(Habit habit) {
        Executors.newSingleThreadExecutor().execute(() -> habitDao.insertHabit(habit));
    }

    public void markProgress(int habitId, String date, boolean completed) {
        Progress progress = new Progress();
        progress.habitId = habitId;
        progress.date = date;
        progress.completed = completed;

        Executors.newSingleThreadExecutor().execute(() -> progressDao.insertProgress(progress));
    }
}
