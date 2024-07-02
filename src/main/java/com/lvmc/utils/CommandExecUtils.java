package com.lvmc.utils;

import cn.hutool.core.util.CharUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lvmc.vo.CommonResultVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/7/12 15:07
 */
@Slf4j
public class CommandExecUtils {

    /**
     * 是否在dockers中运行
     */
    private static AtomicBoolean runInDocker = null;

    private static String ERROR_PREFIX = "exec error:";

    /**
     * 执行命令
     *
     * @param command
     * @return
     */
    public static String exec(String command) {
        return execReturnWithExitValue(command).getResult();
    }

    /**
     * 执行命令并发挥Code
     *
     * @param command
     * @return
     */
    public static CommonResultVo execReturnWithExitValue(String command) {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.startsWith("windows")) {
            return execWindows(command);
        } else if (osName.startsWith("linux")) {
            if (runInDocker()) {
                return execDocker(command);
            } else {
                return execLinux(command);
            }
        } else if (osName.startsWith("mac")) {
            throw new UnsupportedOperationException();
        } else {
            throw new UnsupportedOperationException();
        }
    }


    public static CommonResultVo execDocker(String command) {
        List<String> commands = new ArrayList<>();
        commands.add("bash");
        commands.add("-c");
        if (!command.contains("\"")) {
            command = CharUtil.DOUBLE_QUOTES + command + CharUtil.DOUBLE_QUOTES;
        }
        commands.add("/usr/bin/nsenter --mount=/host/proc/1/ns/mnt sh -c " + command);
        return execCmdReturnExistValue(commands.toArray(new String[]{}), "\n", ERROR_PREFIX);
    }


    public static CommonResultVo execWindows(String command) {
        List<String> commands = new ArrayList<String>();
        commands.add("cmd");
        commands.add("/C");
        commands.add(command);
        return execCmdReturnExistValue(commands.toArray(new String[]{}), "\n", ERROR_PREFIX);
    }

    public static CommonResultVo execLinux(String command) {
        List<String> commands = new ArrayList<String>();
        commands.add("bash");
        commands.add("-c");
        commands.add(command);
        return execCmdReturnExistValue(commands.toArray(new String[]{}), "\n", ERROR_PREFIX);
    }

    private static boolean runInDocker() {
        if (runInDocker != null) {
            return runInDocker.get();
        }
        runInDocker = new AtomicBoolean();
        List<String> commands = new ArrayList<String>();
        commands.add("bash");
        commands.add("-c");
        commands.add("cat /proc/1/cgroup | grep docker");
        String result = execCmdReturnExistValue(commands.toArray(new String[]{}), "\n", "").getResult();
        runInDocker.set(StringUtils.isNotEmpty(result));
        return runInDocker.get();
    }


    /**
     * @param exec     执行的命令
     * @param lineStr  返回结果的分行的符号
     * @param errorStr 错误返回的前缀
     * @return
     */
    private static CommonResultVo execCmdReturnExistValue(String[] exec, String lineStr, String errorStr) {
        Runtime runtime = Runtime.getRuntime();
        StringBuilder outInfo = new StringBuilder();
        int exitValue = 0;

        InputStream in = null;
        BufferedReader br = null;
        try {
            Process proc = runtime.exec(exec);
            in = proc.getInputStream();
            br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            for (String line; (line = br.readLine()) != null; ) {
                outInfo.append(line).append(lineStr);
            }
            in = proc.getErrorStream();
            br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            for (String line = ""; (line = br.readLine()) != null; ) {
                outInfo.append(errorStr).append(line).append(lineStr);
            }
            proc.waitFor();
            exitValue = proc.exitValue();
            log.debug("class[ExecCommand] method[execCmd] " + outInfo);
        } catch (Exception e) {
            log.info("class[ExecCommand] method[execCmd] " + e.getMessage());
        } finally {
            if (in != null && br != null) {
                IOUtils.closeQuietly(in);
                IOUtils.closeQuietly(br);
            }
        }
        return new CommonResultVo(exitValue, outInfo.toString());
    }
}