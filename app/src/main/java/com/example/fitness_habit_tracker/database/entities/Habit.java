@Entity(tableName = "habits")
public class Habit {
    @PrimaryKey(autoGenerate = true)
    public int habitId;

    public int userId;
    public String name;
    public String frequency;
    public long createdAt;
}
