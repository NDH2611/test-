/**
 * Lớp Solution biểu diễn một phân số (rational number) với tử số và mẫu số.
 * Hỗ trợ các phép toán cộng, trừ, nhân, chia, so sánh bằng nhau và rút gọn.
 */
public class Solution {
    private int numerator;
    private int denominator = 1;

    /**
     * Lấy tử số của phân số.
     *
     * @return tử số
     */
    public int getNumerator() {
        return numerator;
    }

    /**
     * Gán giá trị tử số cho phân số.
     *
     * @param numerator tử số mới
     */
    public void setNumerator(int numerator) {
        this.numerator = numerator;
    }

    /**
     * Lấy mẫu số của phân số.
     *
     * @return mẫu số
     */
    public int getDenominator() {
        return denominator;
    }

    /**
     * Gán giá trị mẫu số cho phân số.
     *
     * @param denominator mẫu số mới
     */
    public void setDenominator(int denominator) {
        if (denominator != 0) {
            this.denominator = denominator;
        }
    }

    /**
     * Khởi tạo một phân số với tử số và mẫu số cho trước.
     *
     * @param numerator   tử số
     * @param denominator mẫu số (không được bằng 0)
     */
    public Solution(int numerator, int denominator) {
        this.numerator = numerator;
        if (denominator != 0) {
            this.denominator = denominator;
        }
        else {
            this.denominator = 1;
        }
    }

    /**
     * Tính ước số chung lớn nhất (GCD) của hai số nguyên.
     *
     * @param a số nguyên thứ nhất
     * @param b số nguyên thứ hai
     * @return ước số chung lớn nhất của a và b
     */
    public int gcd(int a, int b) {
        long x = Math.abs((long) a);
        long y = Math.abs((long) b);
        while (y != 0) {
            long r = x % y;
            x = y;
            y = r;
        }
        return (int) x;
    }

    /**
     * Rút gọn phân số (numerator/denominator) về dạng tối giản.
     *
     * @param numerator   tử số
     * @param denominator mẫu số (không được bằng 0)
     * @return phân số mới đã được rút gọn
     */
    public Solution reduce() {
        if (denominator == 0) {
            denominator = 1;
        }
        int d = gcd(Math.abs(numerator), Math.abs(denominator));
        numerator /= d;
        denominator /= d;
        if (denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }
        return this;
    }

    /**
     * Cộng hai phân số.
     *
     * @param other phân số cần cộng
     * @return kết quả cộng (chưa chắc đã rút gọn)
     */
    public Solution add(Solution other) {
        int newNum = numerator * other.denominator + denominator * other.numerator;
        int newDen = denominator * other.denominator;
        if (newDen != 0) {
            numerator = newNum;
            denominator = newDen;
        }
        return this.reduce();
    }

    /**
     * Trừ một phân số cho phân số khác.
     *
     * @param other phân số cần trừ
     * @return kết quả trừ (chưa chắc đã rút gọn)
     */
    public Solution subtract(Solution other) {
        int newNum = numerator * other.denominator - denominator * other.numerator;
        int newDen = denominator * other.denominator;
        if (newDen != 0) {
            numerator = newNum;
            denominator = newDen;
        }
        return this.reduce();
    }

    /**
     * Nhân hai phân số.
     *
     * @param other phân số cần nhân
     * @return kết quả nhân (chưa chắc đã rút gọn)
     */
    public Solution multiply(Solution other) {
        int newNum = numerator * other.numerator;
        int newDen = denominator * other.denominator;
        if (newDen != 0) {
            numerator = newNum;
            denominator = newDen;
        }
        return this.reduce();
    }

    /**
     * Chia một phân số cho phân số khác.
     *
     * @param other phân số chia
     * @return kết quả chia (chưa chắc đã rút gọn)
     */
    public Solution divide(Solution other) {
        if (other.numerator == 0) {
            return this;
        }
        int newNum = numerator * other.denominator;
        int newDen = denominator * other.numerator;
        if (newDen != 0) {
            numerator = newNum;
            denominator = newDen;
        }
        return this.reduce();
    }

    /**
     * So sánh hai phân số có bằng nhau hay không.
     *
     * @param obj đối tượng cần so sánh
     * @return true nếu hai phân số bằng nhau, ngược lại false
     */
    public boolean equals(Object obj) {
        if (obj instanceof Solution) {
            Solution other = (Solution) obj;
            return this.numerator * other.denominator == this.denominator * other.numerator;
        }
        return false;
    }
}

