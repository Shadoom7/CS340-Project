import java.util.*;
public class Schedule {
    public static int total; //the total score
    public static int studentnumber;
    public static int coursenumber;
    public static int timeslotnumber;
    public static int roomnumber;
    public static int teachernumber;
    public static int[][] courses;
    public static int[][] preferences;
    public static int[][] schedule;
    public static int[][] rooms;
    public static int[] banneddueT;
    public static int[][] baneachslot;
    public static int[][] candidates;
    public static int[][] studentstime;

    public Schedule(int[][] preference, int[] timeslot, int[][] room, int[][]teacher){
      studentnumber = preference.length;
      coursenumber = teacher.length*2;
      teachernumber = coursenumber/2; //seems redundant, why not just teacher.length
      timeslotnumber = timeslot.length;
      roomnumber = room.length;
      total = 0;
      courses = new int[coursenumber][6];
      preferences = new int[studentnumber][4];
      schedule = new int[roomnumber][timeslotnumber];
      rooms = new int[roomnumber][2];
      banneddueT = new int[coursenumber];
      baneachslot = new int[timeslotnumber][coursenumber];
      candidates = new int[coursenumber][studentnumber];
      studentstime = new int [studentnumber][timeslotnumber];

      for (int i = 0; i < coursenumber; i++){
        courses[i][0] = i;
        courses[i][1] = 0;
        courses[i][2] = 0;
      }
      for (int i = 0; i < studentnumber; i++){
        for (int j = 0; j < 4; j++){
          preferences[i][j] = preference[i][j];
          candidates[preference[i][j]][i] = 1;
        }
      }
      for (int i = 0; i< roomnumber; i++){
        rooms[i][0] = i;
        int k = room[i][1];
        rooms[i][1] = k;
      }
      for (int i = 0; i < teachernumber; i++){
        int a = teacher[i][0];
        int b = teacher[i][1];
        courses[a][4] = i;
        courses[b][4] = i;
        banneddueT[a] = b;
        banneddueT[b] = a;
      }
      for (int i = 0; i< studentnumber;i++){
        for(int j = 0; j< timeslotnumber;j++){
          studentstime[i][j] = -1;
        }
      }
    }

    public static void assigncourserank(){
      for (int i = 0; i < studentnumber; i++)
      {
        for(int j = 0; j < 4; j++)
        {
          int a = preferences[i][j];
          courses[a][1]++;
        }
      }
    }
    public static class SortByRank implements Comparator<int[]>{
      public int compare(int[] a,int[] b){
        if(a[1] > b[1]){
          return -1;
        }else if(a[1]<b[1]){
          return 1;
        }else {
          return 0;
        }
      }
    }
    public static void generate(){
      assigncourserank();
      Arrays.sort(courses,new SortByRank());
      Arrays.sort(rooms,new SortByRank());
      int currentcourse = 1;
      OUT:
      for(int i = 0; i < roomnumber; i++){
        for(int j = 0; j < timeslotnumber; j++){
          int a = 0;
          while(baneachslot[j][courses[a][0]] != 0 || courses[a][5] != 0){
            a++;
            if(a==coursenumber){
              break OUT;
            }
          }
          baneachslot[j][courses[a][0]] = 1;
          baneachslot[j][banneddueT[courses[a][0]]] = 1;
          courses[a][5] = 1;
          courses[a][2] = j;
          courses[a][3] = rooms[i][0];
          int courseNo = courses[a][0];
          int k = 0;
          int ii = 0;
          schedule[i][j] = courseNo;
          while(k<rooms[i][1] && ii<studentnumber){
            if(candidates[courseNo][ii]==1){
              if(studentstime[ii][j]==-1){
                studentstime[ii][j]=courseNo;
                k++;
                candidates[courseNo][ii] = 0;
              }
            }
            ii++;
          }
          total += k;
          //courses[a][1] -= k;
        }
      }
    }
    public static int getschedule(){
      return total;
    }
}
