package com.web.test.application.test;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Gongbiaowang {
    //  @Autowired
//  private static CsresMapper csresMapper;
    //��ȡ�����ݴ�ŵ���Ŀ¼��
    private static String savepath = "D:/皇马换地方大干/";
    //�ȴ���ȡ��url
    private static List<String> allwaiturl = new ArrayList<>();
    //��ȡ����url
    private static Set<String> alloverurl = new HashSet<>();
    //��¼����url����Ƚ�����ȡ�ж�
    private static Map<String, Integer> allurldepth = new HashMap<>();
    //��ȡ�����
    private static int maxdepth = 10;
    //�������󣬰��������̵߳ĵȴ�����
    private static Object obj = new Object();
    //��¼���߳���5��
    private static int MAX_THREAD = 20;
    //��¼���е��߳���
    private static int count = 0;
    //��¼��վ��
    private static int num = 0;

    /**
     * ��ҳ������ȡ
     *
     * @param strurl
     * @param depth
     */
    public static void workurl(String strurl, int depth) {
        //�жϵ�ǰurl�Ƿ���ȡ��
        if (!(alloverurl.contains(strurl) || depth > maxdepth)) {
            //����߳��Ƿ�ִ��
            System.out.println("��ǰִ�У�" + Thread.currentThread().getName() + " ��ȡ�̴߳�����ȡ��" + strurl + "��ȣ�" + depth);
            //����url��ȡ���Ķ���
            try {
                URL url = new URL(strurl);
                //ͨ��url��������ҳ������
                URLConnection conn = url.openConnection();
                //ͨ������ȡ����ҳ���ص�����
                InputStream is = conn.getInputStream();
                //��ȡtext���͵�����
                if (conn.getContentType().startsWith("text")) {

                }
                System.out.println("---->" + conn.getContentEncoding());
                //һ�㰴�ж�ȡ��ҳ���ݣ����������ݷ���
                //�����BufferedReader��InputStreamReader���ֽ���ת��Ϊ�ַ����Ļ�����
                //����ת��ʱ����Ҫ��������ʽ����
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "GB2312"));

                //���ж�ȡ����ӡ
                String line = null;
                //������ʽ��ƥ�������ȡ����ҳ������
                Pattern pu = Pattern.compile("<a href=\".+\" class=\"sh14lian\">");
                //ƥ������
                Pattern ps1 = Pattern.compile("<td.*><font color=\".*\">");
                Pattern ps2 = Pattern.compile("<td.*><font color=\".*\">.*");
                //����һ������������ڱ����ļ�,�ļ���Ϊִ��ʱ�䣬�Է��ظ�
                PrintWriter pw = null;
                if (strurl.indexOf("Chtype") != -1) {
                    pw = new PrintWriter(new File(savepath + System.currentTimeMillis() + "-" + depth + "-" + strurl.substring(strurl.indexOf("Chtype/") + 7, strurl.indexOf(".html")) + ".txt"));
                }
                boolean bt = false;
                boolean bn = false;
                int i = 0;
                while ((line = br.readLine()) != null) {
//                    System.out.println(line);
                    //��д����ƥ�䳬���ӵ�ַ

                    if (strurl.indexOf("Chtype") != -1) {
                        if (bt) {
                            pw.print(line + "', '");
                            bt = false;
                        }
                        if (bn) {
                            if (line.indexOf("<") != -1) {
                                line = line.substring(0, line.indexOf("<"));
                                pw.print(line + "', '");
                                bn = false;
                            } else {
                                pw.print(line);
                            }
                        }
//                        pw.println(line);
                        Matcher ms1 = ps1.matcher(line);
                        if (ms1.find()) {
                            Matcher ms2 = ps2.matcher(line);
                            if (ms2.find()) {
                                String s = ms2.group();
                                s = s.substring(s.indexOf("0\">") + 3);
                                if (s.indexOf("<") != -1) {
                                    s = s.substring(0, s.indexOf("<"));
                                } else {
                                    if (i == 1) {
                                        bn = true;
                                    }
                                }
                                if (i == 0) {
                                    /*pw.print("INSERT INTO " +
                                            "csres " +
                                            "(csres_num," +
                                            "csres_name," +
                                            "csres_department," +
                                            "csres_date," +
                                            "csres_state)" +
                                            "VALUES('"+s+"', '");*/
                                    System.out.println("res :  " + s);
                                } else if (i == 1) {
                                    if (bn) {
                                        pw.print(s);
                                    } else {
                                        pw.print(s + "', '");
                                    }
                                } else if (i == 4) {
                                    pw.println(s + "');");
                                } else if (i == 2) {
                                    bt = true;
                                } else {
                                    pw.print(s + "', '");
                                }
                                i++;
                            }
                        }
                        if (i == 5) {
                            i = 0;
                        }

                    }


                    Matcher m = pu.matcher(line);

                    while (m.find()) {
                        String href = m.group();
                        //�ҵ������ӵ�ַ����ȡ�ַ���
                        //��������
                        href = href.substring(href.indexOf("href="));
                        if (href.charAt(5) == '\"') {
                            href = href.substring(6);
                        } else {
                            href = href.substring(5);
                        }
                        //��ȡ�����Ż��߿ո���ߵ�">"����
                        try {
                            href = href.substring(0, href.indexOf("\""));
                        } catch (Exception e) {
                            try {
                                href = href.substring(0, href.indexOf(" "));
                            } catch (Exception e1) {
                                href = href.substring(0, href.indexOf(">"));
                            }
                        }
                        href = "http://www.csres.com" + href;
                        if (href.startsWith("http:") || href.startsWith("https:")) {
                    /*
                    //�������ҳ���ڵ�����
                    //System.out.println(href);
                    //��url��ַ�ŵ�������
                    allwaiturl.add(href);
                    allurldepth.put(href,depth+1);
                    */
                            //����addurl����
                            addurl(href, depth);
                        }

                    }

                }
                pw.close();
                br.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }
            //����ǰurl���е�alloverurl��
            alloverurl.add(strurl);
            System.out.println(strurl + "��ҳ��ȡ��ɣ�����ȡ������" + alloverurl.size() + "��ʣ����ȡ������" + allwaiturl.size() + "����ҳ��" + num);
        }
        /*
        //�õݹ�ķ���������ȡ��������
        String nexturl=allwaiturl.get(0);
        allwaiturl.remove(0);
        workurl(nexturl,allurldepth.get(nexturl));
        */
        if (allwaiturl.size() > 0) {
            synchronized (obj) {
                obj.notify();
            }
        } else {
            System.out.println("��ȡ����......." + num);
        }

    }

    /**
     * ����ȡ��url����ȴ������У�ͬʱ�ж��Ƿ��Ѿ��Ź�
     *
     * @param href
     * @param depth
     */
    public static synchronized void addurl(String href, int depth) {
        //��url�ŵ�������
        allwaiturl.add(href);
        //�ж�url�Ƿ�Ź�
        if (!allurldepth.containsKey(href)) {
            allurldepth.put(href, depth + 1);
            if (href.indexOf("Chtype") != -1) {
                num++;
            }
        }
    }

    /**
     * �Ƴ���ȡ��ɵ�url����ȡ��һ��δ��ȡ��url
     *
     * @return
     */
    public static synchronized String geturl() {
        String nexturl = allwaiturl.get(0);
        allwaiturl.remove(0);
        return nexturl;
    }

    /**
     * �̷߳�������
     */
    public class MyThread extends Thread {
        @Override
        public void run() {
            //�趨һ����ѭ�������߳�һֱ����
            while (true) {
                //�ж��Ƿ������ӣ������ȡ
                if (allwaiturl.size() > 0) {
                    //��ȡurl���д���
                    String url = geturl();
                    //����workurl������ȡ
                    workurl(url, allurldepth.get(url));
                } else {
                    System.out.println("��ǰ�߳�׼���������ȴ�������ȡ��" + this.getName());
                    count++;
                    //����һ���������߳̽���ȴ�״̬����wait����
                    synchronized (obj) {
                        try {
                            obj.wait();
                        } catch (Exception e) {

                        }
                    }
                    count--;
                }
            }
        }

    }

    /**
     * @param args 大干入口
     */
    public static void main(String[] args) {
        //ȷ����ȡ����ҳ��ַ���˴�Ϊ��������ҳ�ϵ�ͼ������ȥ����ҳ
        //��ַΪ        http://book.dangdang.com/
//        String strurl="http://search.dangdang.com/?key=%BB%FA%D0%B5%B1%ED&act=input";
        String strurl = "http://www.csres.com/sort/industry.jsp";
        //����ip
        System.getProperties().setProperty("http.proxyHost", "14.115.105.208");
        System.getProperties().setProperty("http.proxyPort", "808");
        //workurl(strurl,1);
        addurl(strurl, 0);
        for (int i = 0; i < MAX_THREAD; i++) {
            new Gongbiaowang().new MyThread().start();
        }
    }
}
