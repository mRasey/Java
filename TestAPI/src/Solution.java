public class Solution {
    public String longestPalindrome(String s) {
        if(s.length() == 1)
            return s;

        String recordStr = s.charAt(0) + "";

        int i;
        for(i = s.length() / 2; i >= 0; i--) {
            if(recordStr.length() >= 2 * i)
                continue;
            String nowStr;
            int m;
            int n;
            if(i + 1 <= s.length() - 1 && i - 1 >= 0
                    && s.charAt(i - 1) == s.charAt(i + 1)) {
                nowStr = "" + s.charAt(i);
                m = i - 1;
                n = i + 1;
                while(m >= 0 && n <= s.length() - 1 && s.charAt(m) == s.charAt(n)) {
                    nowStr += s.charAt(n);
                    m--;
                    n++;
                }
                nowStr = reverse(nowStr) + nowStr.substring(1);
                if(nowStr.length() > recordStr.length()) {
                    recordStr = nowStr;
                }
            }
            if((i + 1 <= s.length() - 1) && s.charAt(i) == s.charAt(i + 1)) {
                nowStr = "" + s.charAt(i + 1);
                m = i - 1;
                n = i + 2;
                while(m >= 0 && n <= s.length() - 1 && s.charAt(m) == s.charAt(n)) {
                    nowStr += s.charAt(n);
                    m--;
                    n++;
                }
                nowStr = reverse(nowStr) + nowStr;
                if(nowStr.length() > recordStr.length()) {
                    recordStr = nowStr;
                }
            }
            if((i -1 >= 0) && s.charAt(i - 1) == s.charAt(i)) {
                nowStr = "" + s.charAt(i);
                m = i - 2;
                n = i + 1;
                while(m >= 0 && n <= s.length() - 1 && s.charAt(m) == s.charAt(n)) {
                    nowStr += s.charAt(n);
                    m--;
                    n++;
                }
                nowStr = reverse(nowStr) + nowStr;
                if(nowStr.length() > recordStr.length()) {
                    recordStr = nowStr;
                }
            }
        }




        for(i = s.length() / 2; i < s.length(); i++) {
            if(recordStr.length() >= 2 * (s.length() - i))
                continue;
            String nowStr;
            int m;
            int n;
            if(i + 1 <= s.length() - 1 && i - 1 >= 0
                    && s.charAt(i - 1) == s.charAt(i + 1)) {
                nowStr = "" + s.charAt(i);
                m = i - 1;
                n = i + 1;
                while(m >= 0 && n <= s.length() - 1 && s.charAt(m) == s.charAt(n)) {
                    nowStr += s.charAt(n);
                    m--;
                    n++;
                }
                nowStr = reverse(nowStr) + nowStr.substring(1);
                if(nowStr.length() > recordStr.length()) {
                    recordStr = nowStr;
                }
            }
            if((i + 1 <= s.length() - 1) && s.charAt(i) == s.charAt(i + 1)) {
                nowStr = "" + s.charAt(i + 1);
                m = i - 1;
                n = i + 2;
                while(m >= 0 && n <= s.length() - 1 && s.charAt(m) == s.charAt(n)) {
                    nowStr += s.charAt(n);
                    m--;
                    n++;
                }
                nowStr = reverse(nowStr) + nowStr;
                if(nowStr.length() > recordStr.length()) {
                    recordStr = nowStr;
                }
            }
            if((i -1 >= 0) && s.charAt(i - 1) == s.charAt(i)) {
                nowStr = "" + s.charAt(i);
                m = i - 2;
                n = i + 1;
                while(m >= 0 && n <= s.length() - 1 && s.charAt(m) == s.charAt(n)) {
                    nowStr += s.charAt(n);
                    m--;
                    n++;
                }
                nowStr = reverse(nowStr) + nowStr;
                if(nowStr.length() > recordStr.length()) {
                    recordStr = nowStr;
                }
            }
        }



        return recordStr;
    }

    public String reverse(String s) {
        String out = "";
        for(int i = s.length() - 1; i >= 0; i--)
            out += s.charAt(i);
        return out;
    }

    public boolean judge(String s) {
        int m = 0;
        int n = s.length() - 1;
        while(m > n) {
            if(s.charAt(m) != s.charAt(n))
                return false;
            m++;
            n--;
        }
        return true;
    }

    public static void main(String[] args) {
//        String test = "hellohello";
//        System.out.println(test.substring(test.indexOf("h", 2)));
        Solution solution = new Solution();
        String in = "abababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababa";
        System.out.println(solution.judge(solution.longestPalindrome(in)));
//        System.out.println(new Solution().judge("bb"));
//        System.out.println("bb".substring(0, 2));
    }
}
