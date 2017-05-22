package id.kalender.kalenderindonesia;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;
import id.kalender.kalenderindonesia.decorators.EventDecorator;
import id.kalender.kalenderindonesia.decorators.HighlightWeekendsDecorator;
import id.kalender.kalenderindonesia.decorators.OneDayDecorator;


public class MainActivity extends AppCompatActivity implements OnDateSelectedListener {

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    @Bind(R.id.calendarView)
    MaterialCalendarView widget;

    @Bind(R.id.tv_event)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        widget.addDecorators(
                new TodayDecorator(),
                new HighlightWeekendsDecorator(),
                oneDayDecorator
        );


        widget.setOnDateChangedListener(this);

        //Setup initial text
        textView.setText(getSelectedDatesString());

        //Pemanggilan API
        new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());

    }

    //Class untuk hari ini
    private class TodayDecorator implements DayViewDecorator {

        private final CalendarDay today;
        private final Drawable backgroundDrawable;

        public TodayDecorator() {
            today = CalendarDay.today();
            backgroundDrawable = getResources().getDrawable(R.drawable.today_circle_background);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return today.equals(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setBackgroundDrawable(backgroundDrawable);
        }
    }



    //Fungsi saat tap setiap tgl & tgl libur
    private String getSelectedDatesString() {

        CalendarDay date = widget.getSelectedDate();

        // System.out.println("date = " + date);

        if(date == null){
            return "Hi";
        }else if(date.toString().equals("CalendarDay{2017-0-1}")){
            return "Tahun Baru 2017";
        }else if(date.toString().equals("CalendarDay{2017-0-28}")){
            return "Tahun Baru Imlek 2568";
        }else if(date.toString().equals("CalendarDay{2017-2-28}")){
            return "Hari Raya Nyepi";
        }else if(date.toString().equals("CalendarDay{2017-3-14}")){
            return "Wafat Isa Almasih";
        }else if(date.toString().equals("CalendarDay{2017-3-24}")){
            return "Isra Mi'raj Nabi Muhammad SAW (27 Rajab 1438 H)";
        }else if(date.toString().equals("CalendarDay{2017-4-1}")){
            return "Hari Buruh Internasional";
        }else if(date.toString().equals("CalendarDay{2017-4-11}")){
            return "Hari Raya Waisak 2017";
        }else if(date.toString().equals("CalendarDay{2017-4-25}")){
            return "Kenaikan Isa Almasih";
        }else if(date.toString().equals("CalendarDay{2017-5-1}")){
            return "Hari Lahir Pancasila";
        }else if(date.toString().equals("CalendarDay{2017-5-25}")){
            return "Hari Raya Idul Fitri 1438 Hijriyah";
        }else if(date.toString().equals("CalendarDay{2017-5-26}")){
            return "Hari Raya Idul Fitri 1438 Hijriyah";
        }else if(date.toString().equals("CalendarDay{2017-5-27}")){
            return "Cuti Bersama";
        }else if(date.toString().equals("CalendarDay{2017-5-28}")){
            return "Cuti Bersama";
        }else if(date.toString().equals("CalendarDay{2017-5-29}")){
            return "Cuti Bersama";
        }else if(date.toString().equals("CalendarDay{2017-5-30}")){
            return "Cuti Bersama";
        }else if(date.toString().equals("CalendarDay{2017-7-17}")){
            return "Hari Kemerdekaan Republik Indonesia";
        }else if(date.toString().equals("CalendarDay{2017-8-1}")){
            return "Hari Raya Idul Adha 1438 Hijriyah";
        }else if(date.toString().equals("CalendarDay{2017-8-21}")){
            return "Tahun Baru Islam (1439 Hijriyah)";
        }else if(date.toString().equals("CalendarDay{2017-11-1}")){
            return "Maulid Nabi Muhammad SAW (12 Rabiul Awal 1439 H)";
        }else if(date.toString().equals("CalendarDay{2017-11-25}")){
            return "Hari Raya Natal 2017";
        }else if(date.toString().equals("CalendarDay{2017-11-26}")){
            return "Cuti Bersama";
        }

        return FORMATTER.format(date.getDate());

    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

        //If you change a decorate, you need to invalidate decorators
        oneDayDecorator.setDate(date.getDate());
        widget.invalidateDecorators();

        //Saat tap tgl libur
        textView.setText(getSelectedDatesString());

    }

    /**
     * Simulate an API call to show how to add decorators
     */
    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //----------------------------
            //API hari libur INDONESIA
            //----------------------------

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();


            // Bulan Januari
            calendar.set(2017, 0, 1);
            CalendarDay tahunBaru = CalendarDay.from(calendar);
            dates.add(tahunBaru);

            calendar.set(2017, 0, 28);
            CalendarDay imlek = CalendarDay.from(calendar);
            dates.add(imlek);


            // Bulan Maret
            calendar.set(2017, 2, 28);
            CalendarDay nyepi = CalendarDay.from(calendar);
            dates.add(nyepi);


            // Bulan April
            calendar.set(2017, 3, 14);
            CalendarDay wafatIsaAlmasih = CalendarDay.from(calendar);
            dates.add(wafatIsaAlmasih);

            calendar.set(2017, 3, 24);
            CalendarDay israMiraj = CalendarDay.from(calendar);
            dates.add(israMiraj);


            // Bulan Mei
            calendar.set(2017, 4, 1);
            CalendarDay buruh = CalendarDay.from(calendar);
            dates.add(buruh);

            calendar.set(2017, 4, 11);
            CalendarDay waisak = CalendarDay.from(calendar);
            dates.add(waisak);

            calendar.set(2017, 4, 25);
            CalendarDay kenaikanIsaAlmasih = CalendarDay.from(calendar);
            dates.add(kenaikanIsaAlmasih);


            // Bulan Juni
            calendar.set(2017, 5, 1);
            CalendarDay pancasila = CalendarDay.from(calendar);
            dates.add(pancasila);

            calendar.set(2017, 5, 25);
            CalendarDay idulFitri1 = CalendarDay.from(calendar);
            dates.add(idulFitri1);

            calendar.set(2017, 5, 26);
            CalendarDay idulFitri2 = CalendarDay.from(calendar);
            dates.add(idulFitri2);

            calendar.set(2017, 5, 27);
            CalendarDay cutiBersama1_1 = CalendarDay.from(calendar);
            dates.add(cutiBersama1_1);

            calendar.set(2017, 5, 28);
            CalendarDay cutiBersama1_2 = CalendarDay.from(calendar);
            dates.add(cutiBersama1_2);

            calendar.set(2017, 5, 29);
            CalendarDay cutiBersama1_3 = CalendarDay.from(calendar);
            dates.add(cutiBersama1_3);

            calendar.set(2017, 5, 30);
            CalendarDay cutiBersama1_4 = CalendarDay.from(calendar);
            dates.add(cutiBersama1_4);


            // Bulan Agustus
            calendar.set(2017, 7, 17);
            CalendarDay merdeka = CalendarDay.from(calendar);
            dates.add(merdeka);


            // Bulan September
            calendar.set(2017, 8, 1);
            CalendarDay idulAdha = CalendarDay.from(calendar);
            dates.add(idulAdha);

            calendar.set(2017, 8, 21);
            CalendarDay tahunBaruIslam = CalendarDay.from(calendar);
            dates.add(tahunBaruIslam);


            // Bulan Desember
            calendar.set(2017, 11, 1);
            CalendarDay mailidArrosul = CalendarDay.from(calendar);
            dates.add(mailidArrosul);

            calendar.set(2017, 11, 25);
            CalendarDay natal = CalendarDay.from(calendar);
            dates.add(natal);

            calendar.set(2017, 11, 26);
            CalendarDay cutiBersama2_1 = CalendarDay.from(calendar);
            dates.add(cutiBersama2_1);


            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            widget.addDecorator(new EventDecorator(Color.RED, calendarDays));

        }


    }


}