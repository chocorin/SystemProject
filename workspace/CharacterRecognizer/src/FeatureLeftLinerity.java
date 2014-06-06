import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Color;


//
//	�����摜�̍��ӂ̒����x������ʂƂ��Čv�Z����N���X
//
class  FeatureLeftLinerity implements FeatureEvaluater
{
	// ���ӂ̒����ƕ����̍���
	protected float  left_length;
	protected float  char_height;
		
	// �����̕Ӂi�摜�̊e�s�̍ł������ɂ���h�b�g�̂w���W�A�s�ɂЂƂ��h�b�g���Ȃ���� -1�j
	protected int  left_x[];	

	
	// �Ō�ɓ����ʌv�Z���s�����摜�i�`��p�j
	protected BufferedImage  last_image;
	
	
	// �����ʂ̖��O��Ԃ�
	public String  getFeatureName()
	{
		return  "���ӂ̒����x�i�����̍��� / �����̕ӂ̒����j";
	}
	
	// �����摜����P�����̓����ʂ��v�Z����
	public float  evaluate( BufferedImage image )
	{
		int  height = image.getHeight();
		int  width = image.getWidth();
		
		// �����̕ӂ����o���i�e�s�̍ł������̃h�b�g�̂w���W�𒲂ׂ�j
				left_x = new int[ height ];
				for ( int y=0; y<height; y++ )
				{
					// �ŏ��͍s�ɍ��s�N�Z�����P���Ȃ����̂Ƃ��� -1 �ŏ�����
					left_x[ y ] = -1;

					// �������珇�ԂɃs�N�Z���𑖍�			
					for ( int x=0; x<width; x++ )
					{
						// �s�N�Z���̐F���擾
						int  color = image.getRGB( x, y );
						
						// �s�N�Z���̐F�����ł���΍ł������̃h�b�g�Ƃ��č��W���L�^
						if ( color == 0xff000000 )
						{
							left_x[ y ] = x;
							break;
						}
					}
				}
				
				//for(int x=0; x<width; x++)
				//{
				//	if(left_x[ y ])
			//	} x�̒l�̃M���b�v���Ȃ����ɂ́H
		
		// �����̍������v�Z
		char_height = 1.0f; // �v����
		int char_n = 0;
		int char_s = 0;
		for(int i=0; i<height; i++) //�ォ��T��
		{
			if(left_x[i] != -1)
			{
				char_n = i ;
				break;
			}
		}
		for(int i=height-1; i>=0; i--) //������T��
		{
			if(left_x[i] != -1)
			{
				char_s = i ;
				break;
			}
		}
		char_height = char_s - char_n;

		
		
		// �����̍����̕ӂ̒������v�Z
		left_length = 0; // �v��
		for(int i=0; i<left_x.length -2; i++) //��Ԃ����̈��ŏI���悤��
		{
		if(left_x[i] != -1){
			int d = Math.abs(left_x[i+1]-left_x[i]);
			left_length += Math.sqrt(1 + d*d);
		}
			
			
		}
		
		
		// �����̍��� / �����̕ӂ̒��� �̔���v�Z		
		float  left_linearity;
		left_linearity = char_height / left_length;
		
		// �摜���L�^�i�`��p�j
		last_image = image;
		
		return  left_linearity;
	}
	
	// �Ō�ɍs���������ʌv�Z�̌��ʂ�`�悷��
	public void  paintImageFeature( Graphics g )
	{
		if ( last_image == null )
			return;
		
		int  ox = 0, oy = 0;
		g.drawImage( last_image, ox, oy, null );
		
		int  x0, y0, x1, y1;
		for ( int y=0; y<left_x.length-1; y++ )
		{
			y0 = y;
			y1 = y+1;
			x0 = left_x[ y0 ];
			x1 = left_x[ y1 ];
			if ( ( x0 != -1 ) && ( x1 != -1 ) )
			{
				// �����̕ӂ̃s�N�Z����`��
				g.setColor( Color.RED );
				g.drawLine( ox + x0, oy + y0, ox + x1, oy + y1 );
			}
		}

		
		String  message;
		g.setColor( Color.RED );
		message = "���ӂ̒���: " + left_length;
		g.drawString( message, ox, oy + 16 );
		message = "�����̍���: " + char_height;
		g.drawString( message, ox, oy + 32 );
		message = "������(�����̍��� / ���ӂ̒���): " + char_height / left_length;
		g.drawString( message, ox, oy + 48 );	
	}
}