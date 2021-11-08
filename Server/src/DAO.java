import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.PreparedStatement;

import java.sql.ResultSet;


public class DAO {


    private Connection conn; // Connection : 데이터베이스에 접근하게 해주는 하나의 객체


    public DAO() { // 생성자를 통해 UserDAO의 인스턴스가 생성되었을 때 자동으로 DB 커넥션이 이루어지도록함

        try {

            String dbURL = "jdbc:mysql://222.104.41.249:9000/myschemas";

            String dbID = "root";

            String dbPassword = "1145";

            Class.forName("com.mysql.cj.jdbc.Driver"); // Class.forName : mysql driver를 찾을 수 있도록 함 *Driver : mysql에 접속할

            // 수 있도록 매개체 역할을 해주는 하나의 라이브러리


            conn = DriverManager.getConnection(dbURL, dbID, dbPassword); // conn : getConnection(db URL, dbID,

            // dbPassword)를 이용하여 DB에 접속하고 접속이 완료가 되면

            // conn 객체안에 접속된 정보가 담긴다

            System.out.println("DataBase connected");

        } catch (Exception e) {
            System.out.println("DB 연결오류");
            e.printStackTrace();

        }

    }


    public boolean signIn(String user_ID, String user_pw) {

        try {


            String SQL = "SELECT user_pw FROM user_info WHERE user_id = ?"; // 실제로 DB에 입력될 명령어를 SQL 문장으로 만듬.

            PreparedStatement pstmt = conn.prepareStatement(SQL);

            pstmt.setString(1, user_ID);

            ResultSet rs = pstmt.executeQuery(); // 어떠한 결과를 받아오는 ResultSet 타입의 rs 변수에 쿼리문을 실행한 결과를 넣어줌

            if (rs.next()) {

                if (rs.getString(1).contentEquals(user_pw)) {

                    System.out.println("일치");

                    return true; // 로그인 성공

                } else {

                    return false; // 비밀번호 불일치

                }

            }

            return false; // 아이디가 없음

        } catch (Exception e) {

            e.printStackTrace();

            return false; // DB 오류

        }

    }


    public boolean signUp(String user_ID, String user_pw, String name) {

        //중복인지 DB오류인지 확인

        try {

            if (overCheck(user_ID)) {

                String SQL = "INSERT INTO user_info VALUES(?,?)";

                PreparedStatement pstmt = conn.prepareStatement(SQL);

                pstmt.setString(1, user_ID);

                pstmt.setString(2, user_pw);

                pstmt.executeUpdate();

                System.out.println("sign up successed");

                return true;

            }

            return false;


        } catch (Exception e) {

            e.printStackTrace();

            return false; // DB 오류

        }

    }


    public boolean overCheck(String user_ID) {


        try {

            String SQL = "SELECT user_ID FROM user_info WHERE user_id = ?"; // 실제로 DB에 입력될 명령어를 SQL 문장으로 만듬.

            PreparedStatement pstmt = conn.prepareStatement(SQL);

            pstmt.setString(1, user_ID);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                if (user_ID.equals(rs.getString(user_ID))) {

                    return false;

                }

            }

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;

        }


    }

}