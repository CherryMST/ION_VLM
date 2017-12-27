package com.qx.framelib.utlis;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * 程序中用到的打印日志类统一使用ZLog
 *
 * @author luohongbo
 */
public class ZLog {
    public static boolean isDebug = true;
    private static final String LOG_TAG = "zooer";

    public static final boolean LOG_FILE = false;//是否记录日志到文件

    public static void v(String tag, String msg) {
        if (isDebug) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

    public static void d(String msg) {
        if (isDebug) {
            Log.e(LOG_TAG, msg);
        }
    }

    /**
     * 将一些日志附加的信息加上，方便日志输出更多的信息
     *
     * @param msg
     * @return
     */
    private static String buildMsg(String msg) {
        StackTraceElement[] trace = new Throwable().fillInStackTrace()
                .getStackTrace();
        String caller = "<unknown>";
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(ZLog.class)) {
                String callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass
                        .lastIndexOf('.') + 1);
                callingClass = callingClass.substring(callingClass
                        .lastIndexOf('$') + 1);

                caller = callingClass + "." + trace[i].getMethodName() + "("
                        + trace[i].getLineNumber() + ")";
                break;
            }
        }
        return String.format("[%s:%d] %s: %s",
                Thread.currentThread().getName(), Thread.currentThread()
                        .getId(), caller, msg);
    }

    /**
     * 打印调用栈
     */
    static public void printCallTraces(String tag) {
        if (isDebug) {
            java.util.Map<Thread, StackTraceElement[]> ts = Thread
                    .getAllStackTraces();
            StackTraceElement[] ste = ts.get(Thread.currentThread());
            v(tag, "======================start============================");
            for (StackTraceElement s : ste) {
                v(tag, s.toString());
            }
            v(tag, "=======================end============================");
        }
    }

    /**
     * 打印日志到文件或者logcat。每行会附上线程、进程相关信息
     *
     * @param tag
     * @param msg           日志信息
     * @param filename      需要保存到的日志文件
     * @param printStack    是否打印调用盏信息
     * @param toLogcat      是否也将日志打印到logcat
     * @param stackToLogcat 详细堆栈是否到logcat？
     */
    public static void fLog(String tag, String msg, String filename,
                            boolean printStack, boolean toLogcat, boolean stackToLogcat) {
        if (!isDebug) {
            return;
        }
        StackTraceElement[] stackTraceElements = Thread.currentThread()
                .getStackTrace();
        StringBuilder stringBuilder = new StringBuilder();
        tag = tag + " " + TimeUtils.getNowTimeMills() + " "
                + android.os.Process.myPid() + "/"
                + Thread.currentThread().getName() + ","
                + Thread.currentThread().getId() + " ";
        stringBuilder.append(tag).append(" -------->start<--------.\n");
        stringBuilder.append(tag).append("msg:").append(msg).append("\n");
        if (toLogcat) {
            d(tag, tag + " msg:" + msg);
        }
        if (printStack) {
            stringBuilder.append(tag).append(" info stack:\n");
            boolean start = false;// 简单跳过自身堆栈信息的打印
            for (StackTraceElement traceElement : stackTraceElements) {
                String stack = traceElement.toString();
                if (start) {
                    stringBuilder.append(tag).append(" ").append(stack)
                            .append("\n");
                }
                if (!start) {
                    if (stack.contains("ZLog")) {
                        start = true;
                    }
                }
            }
        }
        stringBuilder.append(tag).append(" -------->end<--------.\n\n");
        if (stackToLogcat) {
            d(tag, stringBuilder.toString());
        }
        if (!TextUtils.isEmpty(filename)) {
            f(stringBuilder.toString(), filename, true);
        }
    }

    /**
     * 记录异常到文件,一些程序中出现的异常需要记录
     *
     * @param e
     */
    public static void f(final Throwable e) {
        if (isDebug) {
            Writer writer = null;
            PrintWriter printWriter = null;
            try {
                writer = new StringWriter();
                printWriter = new PrintWriter(writer);
                e.printStackTrace(printWriter);
                String msg = writer.toString();
                fLog("czom.zooernet.mall", "exception INFO: " + msg,
                        "dkevin.txt", true, false, true);
            } catch (Exception ignore) {
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException ie) {
                        e.printStackTrace();
                    }
                }
                if (printWriter != null) {
                    printWriter.close();
                }
            }
        }
    }

    public static void f(String msg, String filename, boolean append) {
        writeToFile(msg, filename, append);
    }

    public static void f(String msg, String filename) {
        writeToFile(msg, filename, false);
    }

    public static void writeToFile(String msg, String filename, boolean append) {
        if (isDebug) {
            BufferedWriter bos = null;
            try {
                bos = new BufferedWriter(new FileWriter(FileUtils.getLogDir()
                        + "/" + filename, append));
                bos.write(TimeUtils.getNowTime() + " " + msg + "\r\n");
                bos.flush();
            } catch (Exception ignore) {
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

}
