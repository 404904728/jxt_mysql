package core.cq.hmq.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 何建
 * @version 1.0 <br>
 * 生成验证码的servlet
 *
 */
public class AuthImg extends HttpServlet {
	private Font mFont = new Font("Arial Black", Font.PLAIN, 16);

	public void init() throws ServletException {
		super.init();
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

		int width = 60, height = 18;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics g = image.getGraphics();
		Random random = new Random();
		g.setColor(new Color(124, 174, 211));
		g.fillRect(0, 0, width, height);
		g.setFont(mFont);

		StringBuilder sRand = new StringBuilder(6);
		for (int i = 0; i < 1; i++) {
			String tmp = getRandomChar();
			sRand.append(tmp);
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110),
					20 + random.nextInt(110)));
			g.drawString(tmp, 15 * i + 5, 15);
		}
		request.getSession().setAttribute("AUTH_IMG_IN_SESSION", sRand.toString().toLowerCase());
		g.dispose();
		ImageIO.write(image, "JPEG", response.getOutputStream());
	}

	private String getRandomChar() {
		String rounmImg="";
		for (int i = 0; i < 4; i++) {
			long itmp = Math.round(Math.random() * 25 + 65);
			if (itmp % 2 == 0) {
				rounmImg+=String.valueOf((char) itmp);
			} else {
				rounmImg+=String.valueOf((char) itmp).toLowerCase();
			}
		}
		return rounmImg;
	}
}
