package team.ngup.douban;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import team.ngup.douban.common.http.HttpClientResult;
import team.ngup.douban.common.http.HttpClientUtils;
import team.ngup.douban.request.DoubanRequest;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangwenwu
 */
public class Login {

    private JPanel loginPanel;
    private JLabel loginStatus;
    private boolean isLogined = false;


    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Login");
        DoubanRequest doubanRequest = new DoubanRequest();
        JSONObject response = doubanRequest.getQr();
        String picUrl = response.getString("img");
        Login login = new Login();

        //login.erweima.setText("<html><img src='"+picUrl+"'></img></html>");
        frame.setContentPane(login.loginPanel);
        frame.add(new LinkLabel("加载二维码以登陆", picUrl, new LoginCallback() {
            @Override
            public void execute() {
                boolean isPending = true;
                while (isPending) {
                    try {
                        Thread.sleep(2000);
                        String code = response.getString("code");
                        Map<String, String> headers = new HashMap<>();
                        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");
                        headers.put("Content-Type", "application/x-www-form-urlencoded");
                        headers.put("Accept", "application/json");
                        headers.put("Origin", "https://accounts.douban.com");
                        Map<String, String> params = new HashMap<>();
                        params.put("ck", "");
                        params.put("code", code);
                        HttpClientResult result = HttpClientUtils.doGet("https://accounts.douban.com/j/mobile/login/qrlogin_status", headers, params);
                        if (result.getCode() == 200) {
                            JSONObject resultObject = JSONObject.parseObject(result.getContent());
                            if ("success".equals(resultObject.getString("status"))) {
                                if ("login".equals(resultObject.getJSONObject("payload").getString("login_status"))) {
                                    isPending = false;
                                    System.out.println("已登陆");
                                    login.loginStatus.setText("已登陆");
                                    login.loginStatus.setVisible(true);
                                    login.isLogined = true;
                                    System.out.println("正在获取用户信息");
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // 获取用户信息
                                                JSONObject userInfo = doubanRequest.getUserInfo();
                                                System.out.println(userInfo);
                                                login.loginStatus.setText(userInfo.getString("name"));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                System.out.println("正在获取私人兆赫");
                                                // 播放私人兆赫
                                                JSONArray songs = doubanRequest.getSiRen().getJSONArray("song");
                                                System.out.println(songs);
                                                // Clip clip = AudioSystem.getClip();
                                                // AudioInputStream inputStream = AudioSystem.getAudioInputStream(new URL(songs.getJSONObject(0).getString("url")));
                                                //clip.open(inputStream);
                                                //clip.start();
//file you want to play
                                               /* URL mediaURL = new URL(songs.getJSONObject(0).getString("url"));
                                                Player mediaPlayer = Manager.createRealizedPlayer(mediaURL);
                                                mediaPlayer.start();*/
                                                /*Media m = new Media(songs.getJSONObject(0).getString("url"));
                                                MediaPlayer player = new MediaPlayer(m);
                                                player.play();*/
                                                Media m = new Media(songs.getJSONObject(0).getString("url"));
                                                MediaPlayer player = new MediaPlayer(m);
                                                MediaView viewer = new MediaView(player);

                                                StackPane root = new StackPane();
                                                Scene scene = new Scene(root);

                                                // center video position
                                                javafx.geometry.Rectangle2D screen = Screen.getPrimary().getVisualBounds();
                                                viewer.setX((screen.getWidth() - login.loginPanel.getWidth()) / 2);
                                                viewer.setY((screen.getHeight() - login.loginPanel.getHeight()) / 2);

                                                // resize video based on screen size
                                                DoubleProperty width = viewer.fitWidthProperty();
                                                DoubleProperty height = viewer.fitHeightProperty();
                                                width.bind(Bindings.selectDouble(viewer.sceneProperty(), "width"));
                                                height.bind(Bindings.selectDouble(viewer.sceneProperty(), "height"));
                                                viewer.setPreserveRatio(true);

                                                // add video to stackpane
                                                root.getChildren().add(viewer);

                                                /*VFXPanel.setScene(scene);
                                                //player.play();
                                                videoPanel.setLayout(new BorderLayout());
                                                videoPanel.add(VFXPanel, BorderLayout.CENTER);*/
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();


                                } else {
                                    System.out.println("还未扫码登陆" + System.currentTimeMillis());
                                }
                            }
                        } else {
                            isPending = false;
                            System.err.println(result.getContent());
                        }
                    } catch (IOException | InterruptedException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        }));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
