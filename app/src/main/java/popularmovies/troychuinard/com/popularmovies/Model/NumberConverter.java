package popularmovies.troychuinard.com.popularmovies.Model;


import android.arch.persistence.room.TypeConverter;

public class NumberConverter {

    @TypeConverter
    public static Number toNumber(Integer integer){
        return integer; }


    @TypeConverter
    public static Integer toInt(Number number){
        return number == null ? null : number.intValue();
    }


}
