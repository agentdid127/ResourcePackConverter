package com.agentdid127.resourcepack.library.utilities;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class PropertiesEx extends Properties {
    private static final char[] hexDigit = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C',
            'D', 'E', 'F' };

    private static void writeComments(BufferedWriter bw, String comments) throws IOException {
        bw.write("#");
        int len = comments.length();
        int current = 0;
        int last = 0;

        for (char[] uu = new char[] { '\\', 'u', '\u0000', '\u0000', '\u0000', '\u0000' }; current < len; ++current) {
            char c = comments.charAt(current);
            if (c > 255 || c == '\n' || c == '\r') {
                if (last != current)
                    bw.write(comments.substring(last, current));

                if (c > 255) {
                    uu[2] = toHex(c >> 12 & 15);
                    uu[3] = toHex(c >> 8 & 15);
                    uu[4] = toHex(c >> 4 & 15);
                    uu[5] = toHex(c & 15);
                    bw.write(new String(uu));
                } else {
                    bw.newLine();
                    if (c == '\r' && current != len - 1 && comments.charAt(current + 1) == '\n')
                        ++current;
                    if (current == len - 1
                            || comments.charAt(current + 1) != '#' && comments.charAt(current + 1) != '!')
                        bw.write("#");
                }

                last = current + 1;
            }
        }

        if (last != current)
            bw.write(comments.substring(last, current));

        bw.newLine();
    }

    public void store(OutputStream out, String comments) throws IOException {
        this.store1(new BufferedWriter(new OutputStreamWriter(out, "8859_1")), comments, true);
    }

    private void store1(BufferedWriter bw, String comments, boolean escUnicode) throws IOException {
        if (comments != null)
            writeComments(bw, comments);

        bw.write("#" + (new Date()).toString());
        bw.newLine();
        synchronized (this) {
            Iterator var5 = this.entrySet().iterator();
            while (true) {
                if (!var5.hasNext())
                    break;

                Map.Entry<Object, Object> e = (Map.Entry) var5.next();
                String key = (String) e.getKey();
                String val = (String) e.getValue();
                // key = this.saveConvert(key, true, escUnicode);
                // val = this.saveConvert(val, false, escUnicode);
                bw.write(key + "=" + val);
                bw.newLine();
            }
        }

        bw.flush();
        bw.close();
    }

    private static char toHex(int nibble) {
        return hexDigit[nibble & 15];
    }
}