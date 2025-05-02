@Entity(tableName = "progress",
        foreignKeys = @ForeignKey(entity = Habit.class,
                                  parentColumns = "habitId",
                                  childColumns = "habitId",
                                  onDelete = ForeignKey.CASCADE))
public class Progress {
    @PrimaryKey(autoGenerate = true)
    public int progressId;

    public int habitId;
    public String date; 
    public boolean completed;
}
