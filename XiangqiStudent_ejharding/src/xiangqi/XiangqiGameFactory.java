/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016-2017 Gary F. Pollice
 *******************************************************************************/

package xiangqi;

import xiangqi.common.*;
import xiangqi.student_ejharding.alpha.XiangqiAlpha;
import xiangqi.student_ejharding.beta.XiangqiBeta;
import xiangqi.student_ejharding.gamma.XiangqiGamma;
/**
 * A simple factory object that creates the appropriate instance of a XiangqiGame.
 * @version Dec 26, 2016
 */
public class XiangqiGameFactory
{
	/**
	 * Factory method that returns an instance of the requested game.
	 * @param version the version requested
	 * @return the instance of the requested game
	 */
	public static XiangqiGame makeXiangqiGame(XiangqiGameVersion version)
	{
		switch(version){
		case ALPHA_XQ: return new XiangqiAlpha();
		case BETA_XQ: return new XiangqiBeta();
		case GAMMA_XQ: return new XiangqiGamma();
		default : return new XiangqiAlpha();
		} 
		
	}
}
