package com.southwind.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author 吕茂陈
 * @date 2021/09/28 21:24
 */
@Aspect
public class TrackCounter {

    private Map<Integer, Integer> trackCounts = new HashMap<>();

    /**
     * args(trackNumber) 是限定符，表明传递给 playTrack() 方法的 int 类型的参数也会传递到通知中去
     *
     * @param trackNumber
     */
    @Pointcut("execution( * com.southwind.service.CompactDisc.playTrack(int))" +
            "&& args(trackNumber)")
    public void trackPlayed(int trackNumber) {
    }

    @Before(value = "trackPlayed(trackNumber)", argNames = "trackNumber")
    public Map<Integer, Integer> countTrack(int trackNumber) {
        Integer currentCount = trackCounts.getOrDefault(trackNumber, 0);
        trackCounts.put(currentCount, currentCount + 1);
        return trackCounts;
    }

}
