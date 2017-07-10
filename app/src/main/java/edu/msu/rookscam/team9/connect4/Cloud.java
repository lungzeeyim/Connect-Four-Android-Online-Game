package edu.msu.rookscam.team9.connect4;

import android.content.Intent;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Ava1oN on 3/21/17.
 */

public class Cloud {

    private static final String MAGIC = "NechAtHa6RuzeR8x";
    private static final String USER = "longziya";
    private static final String PASSWORD = "A47215689";
    private static final String JOIN = "http://webdev.cse.msu.edu/~longziya/cse476/proj2/join.php";
    private static final String SAVE_URL = "http://webdev.cse.msu.edu/~longziya/cse476/step6/hatter-save.php";
    private static final String CREATE_USR = "http://webdev.cse.msu.edu/~longziya/cse476/proj2/create-user.php";
    private static final String LOGIN_USR = "http://webdev.cse.msu.edu/~longziya/cse476/proj2/login-user.php";
    private static final String CHECKSTART = "http://webdev.cse.msu.edu/~longziya/cse476/proj2/create-game.php";
    private static final String PUSH = "http://webdev.cse.msu.edu/~longziya/cse476/proj2/receiver.php";
    private static final String PULL = "http://webdev.cse.msu.edu/~longziya/cse476/proj2/pull.php";
    private static final String END = "http://webdev.cse.msu.edu/~longziya/cse476/proj2/end.php";
    private static final String UTF8 = "UTF-8";
    private String opponent = "";
    private String currUser = "";
    private int gridIndex = 43;
    private String msg = "";


    public boolean createUser(String usr, String psw) {

        String query = CREATE_USR + "?user=" + USER + "&magic=" + MAGIC + "&pw=" + PASSWORD + "&usr=" + usr + "&psw=" + psw;
        InputStream stream = null;
        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();


            int responseCode = conn.getResponseCode();
            //Log.d("rrres", responseCode);
            if(responseCode != HttpURLConnection.HTTP_OK) {
                Log.d("ddd","d");
                return false;
            }

            stream = conn.getInputStream();
            //logStream(stream);

            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xml2 = Xml.newPullParser();
                xml2.setInput(stream, UTF8);

                xml2.nextTag();      // Advance to first tag
                xml2.require(XmlPullParser.START_TAG, null, "hatter");

                String status = xml2.getAttributeValue(null, "status");
                if(status.equals("no")) {
                    return false;
                }

                // We are done
            } catch(XmlPullParserException ex) {
                return false;
            } catch(IOException ex) {
                return false;
            }

        } catch (MalformedURLException e) {
            return false;
        } catch (IOException ex) {
            return false;
        } finally {
            if(stream != null) {
                try {
                    stream.close();
                } catch(IOException ex) {
                    // Fail silently
                }
            }
        }

        return true;
    }

    public boolean loginUser(String user, String password)
    {
        String query = LOGIN_USR + "?user=" + USER + "&magic=" + MAGIC + "&pw=" + PASSWORD + "&usr=" + user + "&psw=" + password;
        InputStream stream = null;
            try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            int responseCode = conn.getResponseCode();
            //Log.d("rrres", responseCode);
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            }

            stream = conn.getInputStream();
            //logStream(stream);

            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xml2 = Xml.newPullParser();
                xml2.setInput(stream, UTF8);

                xml2.nextTag();      // Advance to first tag
                xml2.require(XmlPullParser.START_TAG, null, "connect");

                String status = xml2.getAttributeValue(null, "status");
                if(status.equals("no")) {
                    return false;
                }

                // We are done
            } catch(XmlPullParserException ex) {
                return false;
            } catch(IOException ex) {
                return false;
            }

        } catch (MalformedURLException e) {
            return false;
        } catch (IOException ex) {
            return false;
        } finally {
            if(stream != null) {
                try {
                    stream.close();
                } catch(IOException ex) {
                    // Fail silently
                }
            }
        }

            return true;
    }

    public static void logStream(InputStream stream) {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream));

        Log.e("476", "logStream: If you leave this in, code after will not work!");
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                Log.e("476", line);
            }
        } catch (IOException ex) {
            return;
        }
    }

    public boolean checkStart(String usr) {
        InputStream stream = null;
        String query = CHECKSTART + "?user=" + USER + "&magic=" + MAGIC + "&pw=" + PASSWORD + "&usr=" + usr;
        //InputStream stream = null;
        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            int responseCode = conn.getResponseCode();
            //Log.d("rrres", responseCode);
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            }

            stream = conn.getInputStream();
            //logStream(stream);

            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xml2 = Xml.newPullParser();
                xml2.setInput(stream, UTF8);

                xml2.nextTag();      // Advance to first tag
                xml2.require(XmlPullParser.START_TAG, null, "connect");

                String status = xml2.getAttributeValue(null, "status");
                msg = xml2.getAttributeValue(null, "msg");
                if(status.equals("")) {
                    return false;
                }

                if (msg.equals("start"))
                {
                    setOpponent(xml2.getAttributeValue(null, "op"));
                }

                // We are done
            } catch(XmlPullParserException ex) {
                return false;
            } catch(IOException ex) {
                return false;
            }

        } catch (MalformedURLException e) {
            return false;
        } catch (IOException ex) {
            return false;
        } finally {
            if(stream != null) {
                try {
                    stream.close();
                } catch(IOException ex) {
                    // Fail silently
                }
            }
        }

        return true;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public boolean push(String usr, int grid) {
        InputStream stream = null;
        String index = Integer.toString(grid);
        String query = PUSH + "?user=" + USER + "&magic=" + MAGIC + "&pw=" + PASSWORD +
                "&userName=" + usr + "&gridNum=" + index ;
        //InputStream stream = null;
        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            int responseCode = conn.getResponseCode();
            //Log.d("rrres", responseCode);
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            }

            stream = conn.getInputStream();
            //logStream(stream);

            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xml2 = Xml.newPullParser();
                xml2.setInput(stream, UTF8);

                xml2.nextTag();      // Advance to first tag
                xml2.require(XmlPullParser.START_TAG, null, "connect");

                String status = xml2.getAttributeValue(null, "status");
                if(status.equals("")) {
                    return false;
                }

                // We are done
            } catch(XmlPullParserException ex) {
                return false;
            } catch(IOException ex) {
                return false;
            }

        } catch (MalformedURLException e) {
            return false;
        } catch (IOException ex) {
            return false;
        } finally {
            if(stream != null) {
                try {
                    stream.close();
                } catch(IOException ex) {
                    // Fail silently
                }
            }
        }

        return true;
    }

    public boolean pull() {
        InputStream stream = null;
        String query = PULL + "?user=" + USER + "&magic=" + MAGIC + "&pw=" + PASSWORD +
                "&pull=1";
        //InputStream stream = null;
        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            int responseCode = conn.getResponseCode();
            //Log.d("rrres", responseCode);
            if (responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            }

            stream = conn.getInputStream();
            //logStream(stream);

            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xml2 = Xml.newPullParser();
                xml2.setInput(stream, UTF8);

                xml2.nextTag();      // Advance to first tag
                xml2.require(XmlPullParser.START_TAG, null, "connect");

                String status = xml2.getAttributeValue(null, "status");
                if (status.equals("")) {
                    return false;
                }

                currUser = xml2.getAttributeValue(null, "usr");
                gridIndex = Integer.parseInt(xml2.getAttributeValue(null, "gridNum"));
                // We are done
            } catch (XmlPullParserException ex) {
                return false;
            } catch (IOException ex) {
                return false;
            }

        } catch (MalformedURLException e) {
            return false;
        } catch (IOException ex) {
            return false;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    // Fail silently
                }
            }
        }

        return true;
    }

    public boolean join(String user) {
        InputStream stream = null;
        String query = JOIN + "?user=" + USER + "&magic=" + MAGIC + "&pw=" + PASSWORD +
                "&usr=" + user;

        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            int responseCode = conn.getResponseCode();
            //Log.d("rrres", responseCode);
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            }

            stream = conn.getInputStream();
            //logStream(stream);

        } catch (MalformedURLException e) {
            return false;
        } catch (IOException ex) {
            return false;
        } finally {
            if(stream != null) {
                try {
                    stream.close();
                } catch(IOException ex) {
                    // Fail silently
                }
            }
        }

        return true;
    }

    public boolean end()
    {
        String query = END + "?user=" + USER + "&magic=" + MAGIC + "&pw=" + PASSWORD +
                "&end=1";
        InputStream stream = null;

        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            int responseCode = conn.getResponseCode();
            //Log.d("rrres", responseCode);
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            }

            stream = conn.getInputStream();
            //logStream(stream);

        } catch (MalformedURLException e) {
            return false;
        } catch (IOException ex) {
            return false;
        } finally {
            if(stream != null) {
                try {
                    stream.close();
                } catch(IOException ex) {
                    // Fail silently
                }
            }
        }

        return true;
    }


    public int getGridIndex() {
        return gridIndex;
    }

    public void setGridIndex(int gridIndex) {
        this.gridIndex = gridIndex;
    }

    public String getCurrUser() {
        return currUser;
    }

    public void setCurrUser(String currUser) {
        this.currUser = currUser;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

