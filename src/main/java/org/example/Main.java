package org.example;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {

        //        1) создать дату своего рождения
        //       - вывести на консоль на русском / английском языках
        LocalDate birthday = LocalDate.of(1987, Month.AUGUST, 30);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH);
        System.out.println(formatter.format(birthday));

        DateTimeFormatter formatterLocalized = formatter.withLocale(new Locale("ru"));
        System.out.println(formatterLocalized.format(birthday));

        //        - найти, на какой день недели выпала дата рождения
        System.out.println(birthday.getDayOfWeek());

        //        - вычесть 10 лет из созданной даты, вывести на консоль
        System.out.println(formatter.format(birthday.minusYears(10)));

        //        2) Найти дату последнего воскресенья марта 2023. Посчитать количество дней до него.
        LocalDate date = LocalDate.now();
        LocalDate lastSunday = date.with(Month.MARCH).with(TemporalAdjusters.lastInMonth(DayOfWeek.SUNDAY));
        System.out.println(lastSunday);

        System.out.println("All days in period: " + date.until(lastSunday, ChronoUnit.DAYS));

        //        3) Составить список времен начала всех занятий по Java на февраль, если предположить,
        //        что они будут проходить каждый понедельник/среду c 9:30 CET.
        List<LocalDate> dateList1 = LocalDate.of(2023, 2, 1).with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY))
                .datesUntil(LocalDate.of(2023, 3, 1), Period.ofDays(7)).collect(Collectors.toList());
        List<LocalDate> dateList2 = LocalDate.of(2023, 2, 1).with(TemporalAdjusters.firstInMonth(DayOfWeek.WEDNESDAY))
                .datesUntil(LocalDate.of(2023, 3, 1), Period.ofDays(7)).collect(Collectors.toList());

        List<ZonedDateTime> plan = Stream.of(dateList1, dateList2).flatMap(e -> e.stream()).
                map(el -> ZonedDateTime.of(LocalDateTime.of(el, LocalTime.of(9, 30)), ZoneId.of("CET"))).sorted().collect(Collectors.toList());
        System.out.println(plan);

        //        4)* Рейс из Лос-Анджелеса во Франкфурт отправляется в 15:05 по местному времени и длится 10 ч. 50 м.
        //        Во сколько он прилетит? Написать метод, который мог бы совершать подобные вычисления.
        findArrivalTime(10L, 50L);
    }

    public static void findArrivalTime(Long hours, Long minute) {
        ZoneId startZone = ZoneId.of("America/Los_Angeles");
        ZoneId finishZone = ZoneId.of("Europe/Berlin");
        LocalDateTime date = LocalDateTime.now();
        ZonedDateTime losAngelesDataTime = ZonedDateTime.of(date, startZone);
        ZonedDateTime frankfurtDataTime = ZonedDateTime.of(date, finishZone);
        Duration duration = Duration.between(frankfurtDataTime, losAngelesDataTime);
        LocalTime time = LocalTime.of(15, 05).plusHours(hours).plusMinutes(minute).plusSeconds(duration.getSeconds());
        System.out.println(time);
    }
}