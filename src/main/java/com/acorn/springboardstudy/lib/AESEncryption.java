package com.acorn.springboardstudy.lib;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.io.*;
import java.security.Key;
import java.util.Base64;

public class AESEncryption {
    // AES : 암호화 알고리즘 // Encryption : 암호화
    // Advanced Encryption Standard : 일반적인 발전된 암호화 기술
    // AES : 작동방식 - (모든 암호화=>해시코드) 대칭키, 블럭암호화, 블럭+패딩(블럭남는공간 채우기) // 작동원리 - ECB, CBC, GCM
    /*
    AES (Advanced Encryption Standard): // 진보된 암호화 표준 // 고급(진보된) 암호화 표준 - 1.대칭키암호화 2.블록암호화 방식을 둘다 사용
    1.대칭키 암호화 방식,(암호화와 복구화에 동일한 키를 사용하는 방식) // 해시코드 : 고정된 길이의 암호화된 문자로 바꾸는 것 // user01 -> FQ01ncdkl1 .. (해시코드) // 이때 특정키(암호화키)를 사용하여 암호화와 복구화에 사용한다.
    2.블록 암호화 방식은 암호화 대상(평문)을 고정된 크키(블럭크기)인 블록으로 나누어 암호화 하는 방식 // 암호화 되기전 평문을 블럭으로 나누고 블럭을 기준으로 암호화
      (블럭 크기 128, 192, 256 bit => AES-[128,192,256] : 블럭 크기가 클수록 보안이 높아지고 속도가 느려짐) // 128비트(8비트==1바이트)로 기반 (영문은 1바이트, 한글은 3바이트) // Acorn 아카데미 (블럭)/ 손동홍씨 (블럭)/ 화이팅 (블럭)
        암호화 알고리즘: AES (Advanced Encryption Standard)
        작동 모드: 1) ECB (Electronic Codebook) 개별 블럭을 그대로 암호화 하는 방식 // 블럭을 기준으로 암호화 // 코드가 짧고, 속도가 빠르다. 테스트에서 사용
                 2) CBC(Cipher Block Chaining) 앞블럭의 암호문을 다시 xor 연산으로 암호화, // 앞블럭을 기준으로 암호화 복잡도가 올라간다.
                 3) GCM(Galois/Counter Mode)  CBC에 인증코드를 추가해서 보안을 강화한 방식 // 보안이 제일 강하다. // 1,2,3 번의 기능이 모두 추가된
        패딩 방식: PKCS5Padding 블록 암호화 방식에서 입력 데이터의 길이가 블록 크기와 일치하지 않을 때 남은 공간을 채우기 위한 방법 // 암호화 블럭에 남아있는 (비트)공간을 채우는 것
*/

    // AES : (암호화=>해쉬코드) 대칭키, 블록+패딩, 작동방식(ECB,CBC,GCM)
    // static은 객체를 만들 필요가 없어서 의존성 - 객체주입 x
    private static final String ALGORITHM="AES"; // 알고리즘 AES 선언
    private static final String CIPHER_ALGORITHM="AES/ECB/PKCS5Padding"; // 어떤형식으로 함호화를 할것인가 / 알고리즘. 작동원리.블럭의패딩 설정
    private static final int BLOCK_SIZE=128; // 전역에 선언시 재사용 가능하다.
    private static Key secretKey; // 암호화와 복호화(복구화)에 사용된 대칭키
    // heap 메모리 (new 연산 객체 생성 == 인스턴스 객체(가비지컬렉터에 의해서 지워진다)) , stack 메모리(method 에 정의된 실행된 객체들. CPU 에 연산처리 요청), method 영역(클래스+static 변수)
    // static - JVM 클래스를 로드할때 static 으로 선언된 필드는 메소드 메모리 영역에 저장 => 어디서든 호출이 된다, 지워지지 않는다.
    // 가비지컬렉터가 없으면 직접 메모리를 삭제해야한다. ex) c 언어 - 포인터를 사용하여 메모리를 삭제한다.

    private static final String KEY_FILE_NAME="./secretKey.ser"; // 톰캣서버가 배포되는 위치에 저장 된다.(상대경로라서) // ser ? // /Library/apache-tomcat-9.0.73/bin/catalina.sh run
    static { // JVM 클래스를 로드할때 static 필드를 초기화한다.
        // private static Key secretKey 선언한 값을 try catch 에서 값을 주려고 초기화 한다. // 선언되지 않는 값에 try catch 쓸수없다?
        try { // * 암호화 키를 파일로 저장하는 법
            File secretKeyFile=new File(KEY_FILE_NAME); // 파일이 있으면 exist 라는 함수가 true, 없으면 false
            if(secretKeyFile.exists()){ // 존재하면 => 기존에 있는 파일을 불러와서 오브젝트로 변환
                ObjectInputStream ois=new ObjectInputStream(new FileInputStream(KEY_FILE_NAME)); // 있는것 확인했으니. 무조건 있을것!
                secretKey=(Key)ois.readObject(); // 파일인 key 를 오브젝트로 불러오는것.

            }else{ // 존재하지 않으면 => 키 생성. 오브젝트를 파일로 변환
                KeyGenerator kg=KeyGenerator.getInstance(ALGORITHM); // => AES 로 대칭키 만들 준비 // 키 생성 (간단 알고리즘) // KeyGenerator 간단한 *암호화 알고리즘으로 대칭키를 생성
                kg.init(BLOCK_SIZE);// 대칭키 블럭의 사이즈. 가장작은사이즈가 128
                secretKey=kg.generateKey();// 톰캣 서버가 시작할때 key 가 생성됨
                ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(KEY_FILE_NAME)); // 파일 사용할 준비 // 파일이 없는 경우 에러발생가능 // Object 를 파일로 변환해주는 객체
                oos.writeObject(secretKey); // 파일로 작성
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // 평문 => 암호 암호화
    public static String encryptValue(String value) throws Exception { // 암호화된 value  일반값을 보내면 암호화된 value 를 반환 // 암호화할때 오류가 발생할수있으므로 익셉션 쓰로우
        // Cipher : 암호화와 복호화 하는 클래스 (복잡한 알고리즘 암호화모드. 복호화모드)
        Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM); // 어떤형식으로 함호화를 할것인가 / 알고리즘. 작동원리.블럭의패딩 설정
        cipher.init(Cipher.ENCRYPT_MODE,secretKey); // 해당키로 암호화할 준비(init) // ENCRYPT_MODE 값이 원래 1이다. 1로 써도 되고 ENCRYPT_MODE 으로 써도된다. // 초기화
        // cipher.init(1,secretKey); // 해당키로 암호화할 준비(init) // 1 이 암호화 모드 // 초기화

        // 암호화된 해시코드(바이트 인코딩)
        byte[] encryptBytes=cipher.doFinal(value.getBytes()); // "안녕" 문자열 == {'안','녕'} char[] == {-20,-80,-107,-21,-113,-117} byte[] (부호비트가 있어서 - 마이너스가 있다)
        // => 암호화된 해시코드 바이트로 인코딩이 된다. // 암호화 하겠다. 바이트로 받아야 한다. // 문자열을 바이트로 반환
        // 어떻게? 문자열,캐릭터타입 같다. 캐릭터타입은 바이트로 변환가능하다. // "AB"(문자열) =={'A','B'} (char[] 배열) == {65,66} byte[] 배열
        return Base64.getEncoder().encodeToString(encryptBytes); // 바이트 인코딩 문자열로 반환 // Base64.getEncoder() : 인코딩이 어려운 값들을 인코딩해준다.
    };

    // 암호화 => 평문 복구화
    public static String decryptValue(String encryptValue) throws Exception {
        byte[] encryptBytes=Base64.getDecoder().decode(encryptValue); // 문자열(유니코드)을 => 바이트 인코딩으로 변환 // 바이트 인코딩한걸 다시 디코딩으로 바꾼다. // encryptValue 값을 다시 byte 로 바꿔야 한다.
        Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE,secretKey); // 대칭키로 복호화 준비
        byte[] decryptBytes=cipher.doFinal(encryptBytes); // 유니코드인 바이트 배열을 반환
        return new String(decryptBytes); // 유니코드 바이트 => 문자열
    }



}
