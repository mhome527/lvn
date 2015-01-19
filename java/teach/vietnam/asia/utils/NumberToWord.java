package teach.vietnam.asia.utils;

public class NumberToWord {
	private static String[] ChuSo = { " không", " một", " hai", " ba", " bốn", " năm", " sáu", " bảy", " tám", " chín" };
	private static String[] Tien = { "", " ngàn", " triệu", " tỷ", " ngàn tỷ", " triệu tỷ" };

	public static String getWordFromNumber(String numberVN) {
        Long number;
		if (numberVN.trim().equals(""))
			return "";
        try{
            number = Long.parseLong(numberVN);
        }catch(Exception e){
            return "";
        }
		return getWordFromNumber(number);
	}

	// Hàm đọc số thành chữ
	public static String getWordFromNumber(long numberVN) {
		int lan, i;
		long so;
		String KetQua = "", tmp = "";
		int[] ViTri = new int[6];
		if (numberVN < 0)
			return "không";
		if (numberVN == 0)
			return "không";

		so = numberVN;

		// Kiểm tra số quá lớn
		if (numberVN > 89999999999999L) {
			numberVN = 0;
			return "";
		}
		ViTri[5] = (int) (so / 1000000000000000L);
		so = so - Long.parseLong(ViTri[5] + "") * 1000000000000000L;
		ViTri[4] = (int) (so / 1000000000000L);
		so = so - Long.parseLong(ViTri[4] + "") * +1000000000000L;
		ViTri[3] = (int) (so / 1000000000);
		so = so - Long.parseLong(ViTri[3] + "") * 1000000000;
		ViTri[2] = (int) (so / 1000000);
		ViTri[1] = (int) ((so % 1000000) / 1000);
		ViTri[0] = (int) (so % 1000);
		if (ViTri[5] > 0) {
			lan = 5;
		} else if (ViTri[4] > 0) {
			lan = 4;
		} else if (ViTri[3] > 0) {
			lan = 3;
		} else if (ViTri[2] > 0) {
			lan = 2;
		} else if (ViTri[1] > 0) {
			lan = 1;
		} else {
			lan = 0;
		}
		for (i = lan; i >= 0; i--) {
			tmp = DocSo3ChuSo(ViTri[i]);
			KetQua += tmp;
			if (ViTri[i] != 0)
				KetQua += Tien[i];
			if ((i > 0) && (null != tmp))
				KetQua += ",";
		}

		KetQua = KetQua.trim().replaceAll(",", " ").replaceAll("  ", " ");
		return KetQua.substring(0, 1) + KetQua.substring(1);
	}

	// Hàm đọc số có 3 chữ số
	private static String DocSo3ChuSo(int baso) {
		int tram, chuc, donvi;
		String KetQua = "";
		tram = (int) (baso / 100);
		chuc = (int) ((baso % 100) / 10);
		donvi = baso % 10;
		if ((tram == 0) && (chuc == 0) && (donvi == 0))
			return "";
		if (tram != 0) {
			KetQua += ChuSo[tram] + " trăm";
			if ((chuc == 0) && (donvi != 0))
				KetQua += " linh";
		}
		if ((chuc != 0) && (chuc != 1)) {
			KetQua += ChuSo[chuc] + " mươi";
			if ((chuc == 0) && (donvi != 0))
				KetQua = KetQua + " linh";
		}
		if (chuc == 1)
			KetQua += " mười";
		switch (donvi) {
		case 1:
			if ((chuc != 0) && (chuc != 1)) {
				KetQua += " mốt";
			} else {
				KetQua += ChuSo[donvi];
			}
			break;
		case 5:
			if (chuc == 0) {
				KetQua += ChuSo[donvi];
			} else {
				KetQua += " lăm";
			}
			break;
		default:
			if (donvi != 0) {
				KetQua += ChuSo[donvi];
			}
			break;
		}
		return KetQua;
	}
}
