package com.lvmc.service.impl;

import java.util.List;

import com.lvmc.service.CompactDisc;

import lombok.Builder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 吕茂陈
 * @date 2021/09/29 08:50
 */
@Builder
@Slf4j
@Setter
public class BlankDisc implements CompactDisc {

    private String title;
    private String artist;
    private List<String> tracks;

    @Override
    public void play() {
        log.info("playing " + title + " by " + artist);
        tracks.forEach(e -> log.info("-Track: " + e));
    }

    @Override
    public int playTrack(int i) {
        return i;
    }


}
