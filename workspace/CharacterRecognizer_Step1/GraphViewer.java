import java.awt.*;


//
//  �O���t���ł̂Q�������W��\���N���X
//
class  GraphPoint
{
	public float  x;
	public float  y;
}


//
//  �ȈՃO���t�`��N���X
//
class  GraphViewer
{	
	// �}�`�̎��
	public static final int  FIG_SCATTERED = 1; // �U�z�}
	public static final int  FIG_BAR = 2;       // �_�O���t
	public static final int  FIG_LINE = 3;      // �܂���O���t
	public static final int  FIG_Y_LINE = 4;    // Y���ɕ��s�Ȓ����iX=a�̒����j
	
	//	�}�`�f�[�^��\�������N���X
	class  GraphFigure
	{
		// �}�`�̎��
		public int  type;
		
		// �F
		public Color  color;
		
		// �f�[�^�i�Q�����x�N�g���̔z��j
		public GraphPoint   data[];
		
		// ���E�T�C�Y
		public float  width;
	}

	// �}�`�f�[�^
	GraphFigure  figures[];
	
	// �X�N���[�����̕`��͈�
	int  screen_x0;
	int  screen_y0;
	int  screen_x1;
	int  screen_y1;
	
	// �O���t���̕`��͈�
	float  graph_x0;
	float  graph_y0;
	float  graph_x1;
	float  graph_y1;
	
	// �O���t���̍��W�n�����ʏ�̍��W�n�ւ̕ϊ��W��
	int  screen_ox;
	int  screen_oy;
	float  screen_sx;
	float  screen_sy;
	
	// ����`�悷����W
	float  x_axis_gy; // X����`�悷��Y���W
	float  y_axis_gx; // Y����`�悷��X���W
	
	// �O���t�̃O���b�h�Ԋu
	float  graph_grid_x;
	float  graph_grid_y;


	// �R���X�g���N�^
	public  GraphViewer()
	{
		// �}�`�f�[�^�̔z����������i�Ƃ肠�����}�`�̐��͍ő�10�܂łƂ���j
		figures = new GraphFigure[ 10 ];

		// �`��͈͂�������		
		setGraphAreaAuto();
	}
	
	
	//
	//  �`��̈�̐ݒ�
	//
	
	// ��ʏ�̕`��͈͂̐ݒ�
	public void  setDrawArea( int x0, int y0, int x1, int y1 )
	{
		screen_x0 = x0;
		screen_y0 = y0;
		screen_x1 = x1;
		screen_y1 = y1;
		updateTrans();
	}
	
	// �O���t���̕`��͈͂̐ݒ�
	public void  setGraphArea( float x0, float y0, float x1, float y1, float grid_x, float grid_y )
	{
		graph_x0 = x0;
		graph_y0 = y0;
		graph_x1 = x1;
		graph_y1 = y1;
		graph_grid_x = grid_x;
		graph_grid_y = grid_y;
		updateTrans();
	}
	
	// �O���t���̕`��͈͂�ݒ肳��Ă���}�`���玩���v�Z
	public void  setGraphAreaAuto()
	{
		float   min_x = 0.0f, max_x = 0.0f, min_y = 0.0f, max_y = 0.0f;
		
		// �}�`�f�[�^�͈̔͂𒲂ׂ�
		boolean  is_found_first_figure = false;
		for ( int i=0; i<figures.length; i++ )
		{
			if ( ( figures[ i ] == null ) || ( figures[ i ].data.length == 0 ) )
				continue;
			
			// �}�`�̊e�f�[�^�̍��W�𒲂ׂĔ͈͂��X�V
			GraphFigure  fig = figures[ i ];
			for ( int j=0; j<fig.data.length; j++ )
			{
				GraphPoint  p = fig.data[ j ];
				
				if ( !is_found_first_figure )
				{
					min_x = p.x;  max_x = p.x;
					min_y = p.y;  max_y = p.y;
					is_found_first_figure = true;
				}
				if ( p.x < min_x )
					min_x = p.x;
				if ( p.x > max_x )
					max_x = p.x;
				if ( p.y < min_y )
					min_y = p.y;
				if ( p.y > max_y )
					max_y = p.y;
			}

			// �_�O���t�ł���� Y=0 ���ǉ�
			if ( fig.type == FIG_BAR )
			{			
				if ( min_y > 0.0f )
					min_y = 0.0f;
				if ( max_y < 0.0f)
					max_y = 0.0f;
			}
		}
		
		// ����`�悷����W��ݒ�
		if ( min_x == max_x )
			max_x = min_x + 1.0f;
		if ( min_y == max_y )
			max_y = min_y + 1.0f;
		float  width_x, width_y;
		width_x = max_x - min_x;
		width_y = max_y - min_y;
		min_x -= width_x * 0.1f;
		max_x += width_x * 0.1f;
		min_y -= width_y * 0.1f;
		max_y += width_y * 0.1f;
		if ( ( min_x < 0.0f ) && ( max_x > 0.0f ) )
			y_axis_gx = 0.0f;
		else
			y_axis_gx = min_x + width_x * 0.1f;
		if ( ( min_y < 0.0f ) && ( max_y > 0.0f ) )
			x_axis_gy = 0.0f;
		else
			x_axis_gy = min_y + width_y * 0.1f;
			
		// �O���b�h�������I�Ɍv�Z�i����K���ȋ�Ԑ��Ŋ����āA��ԕ��̗L���������P���ɂ���j
		int  num_segments = 5;
		float  grid_x, grid_y;
		double  place;
		grid_x = (float)( max_x - min_x ) / num_segments;
		place = Math.pow( 10, Math.floor( Math.log( grid_x ) / Math.log( 10 ) ) );
		grid_x = (float)( Math.floor( grid_x / place ) * place );
		grid_y = (float)( max_y - min_y ) / num_segments;
		place = Math.pow( 10, Math.floor( Math.log( grid_y ) / Math.log( 10 ) ) );
		grid_y = (float)( Math.floor( grid_y / place ) * place );
			
		// �͈͂�ݒ�
		setGraphArea( min_x, min_y, max_x, max_y, grid_x, grid_y );
	}
	
	// ���W�v�Z�̂��߂̃p�����^�̍X�V
	protected void  updateTrans()
	{
		screen_sx = ( screen_x1 - screen_x0 ) / ( graph_x1 - graph_x0 );
		screen_sy = - ( screen_y1 - screen_y0 ) / ( graph_y1 - graph_y0 );
		screen_ox = (int)( screen_x0 - graph_x0 * screen_sx );
		screen_oy = (int)( screen_y1 - graph_y0 * screen_sy );
	}
	
	// �`����W�̌v�Z�i�O���t���̍��W�n����ʏ�̍��W�n�ɕϊ��j
	protected Point  getDrawPos( GraphPoint gp )
	{
		Point  sp = new Point();
		sp.x = (int)( gp.x * screen_sx + screen_ox );
		sp.y = (int)( gp.y * screen_sy + screen_oy );
		return  sp;
	}
	
	
	//
	//  �O���t�f�[�^�̒ǉ�
	//
	
	// �}�`�f�[�^�̃N���A
	public void  clearFigure()
	{
		for ( int i=0; i<figures.length; i++ )
			figures[ i ] = null;
	}
	
	// �}�`�f�[�^�̒ǉ�
	public void  addFigure( int type, Color color, GraphPoint data[], float width )
	{
		// �}�`�f�[�^�z��̋󂫃f�[�^��T��
		int  i;
		for ( i=0; i<figures.length; i++ )
		{
			if ( figures[ i ] == null )
				break;
		}
		// �z�񂪑S�Ė��܂��Ă���΃f�[�^��ǉ������I��
		if ( i == figures.length )
			return;
			
		// �}�`�f�[�^��ǉ�
		figures[ i ] = new GraphFigure();
		figures[ i ].type = type;
		figures[ i ].color = color;
		figures[ i ].data = data;
		figures[ i ].width = width;
	}
	
	// �}�`�f�[�^�̒ǉ��i�T�C�Y�̏ȗ��j
	public void  addFigure( int type, Color color, GraphPoint data[] )
	{
		addFigure( type, color, data, 1.0f );
	}
	
	
	//
	//  �`�揈��
	//
	
	// �O���t�S�̂̕`��
	public void  paint( Graphics g )
	{
		// ���W���̕`��
		paintCoords( g );
	
		// �}�`�f�[�^�̕`��
		for ( int i=0; i<figures.length; i++ )
		{
			if ( figures[ i ] != null )
				paintFigure( g, figures[ i ] );
		}
	}
	
	// ���W���̕`��
	protected void  paintCoords( Graphics g )
	{
		g.setColor( Color.BLACK );
		
		// X���EY����`�悷��X�N���[�����W���v�Z
		GraphPoint  axis_gp = new GraphPoint();
		axis_gp.x = y_axis_gx;
		axis_gp.y = x_axis_gy;
		Point  axis_pos = getDrawPos( axis_gp );
		
		// �t�H���g�����擾
		FontMetrics  fm = g.getFontMetrics();

		// X���̕`��
		if ( true )
		{
			// X���̕`��
			g.drawLine( screen_x0, axis_pos.y, screen_x1, axis_pos.y );
			
			// X����̃O���b�h�̕`��
			float  grid_start_x = (float) ( Math.floor( graph_x0 / graph_grid_x ) + 1 ) * graph_grid_x;
			int  num_grid_x = (int) Math.floor( (graph_x1 - grid_start_x ) / graph_grid_x ) + 1;
			for ( int i=0; i<num_grid_x; i++ )
			{
				// �ڐ��̃X�N���[�����W���v�Z
				float  graph_gx = grid_start_x + graph_grid_x * i;
				int  screen_gx = (int)( graph_gx * screen_sx + screen_ox );
				
				// �ڐ���`��
				g.drawLine( screen_gx, axis_pos.y, screen_gx, axis_pos.y+4 );
				
				// ���l��`��
				g.drawString( "" + graph_gx, screen_gx, axis_pos.y + fm.getHeight() );
			}
		}
		// Y���̕`��
		if ( true )
		{
			// ���̕`��
			g.drawLine( axis_pos.x, screen_y0, axis_pos.x, screen_y1 );

			// X����̃O���b�h�̕`��
			float  grid_start_y = (float) ( Math.floor( graph_y0 / graph_grid_y ) + 1 ) * graph_grid_y;
			int  num_grid_y = (int) Math.floor( (graph_y1 - grid_start_y ) / graph_grid_y ) + 1;
			for ( int i=0; i<num_grid_y; i++ )
			{
				// �ڐ��̃X�N���[�����W���v�Z
				float  graph_gy = grid_start_y + graph_grid_y * i;
				int  screen_gy = (int)( graph_gy * screen_sy + screen_oy );
				
				// �ڐ���`��
				g.drawLine( axis_pos.x, screen_gy, axis_pos.x - 4, screen_gy );
				
				// ���l��`��
				String  number = "" + graph_gy;
				g.drawString( number, axis_pos.x - fm.stringWidth( number ) - 4, screen_gy );
			}
		}
	}

	// �}�`�f�[�^�̕`��
	protected void  paintFigure( Graphics g, GraphFigure fig )
	{
		int  i;
		Point  pp = null;
		
		// �`��F��ݒ�
		g.setColor( fig.color );

		// �}�`�f�[�^�̊e�l��`��
		for ( i=0; i<fig.data.length; i++ )
		{
			// �f�[�^�̉�ʏ�ł̍��W�l���v�Z
			Point  p = getDrawPos( fig.data[ i ] );
			
			// �U�z�}��`��
			if ( fig.type == FIG_SCATTERED )
			{
				g.fillOval( p.x, p.y, 2 + (int)fig.width, 2 + (int)fig.width );
			}
			
			// �_�O���t��`��
			if ( fig.type == FIG_BAR )
			{
				GraphPoint  bar_base = new GraphPoint();
				bar_base.x = fig.data[ i ].x - fig.width * 0.5f;
				bar_base.y = 0.0f;
				
				Point  pb = getDrawPos( bar_base );
				if ( pb.y > p.y )
					g.fillRect( pb.x, p.y, ( p.x - pb.x ) * 2, pb.y - p.y );				
			}
			
			// �܂���O���t��`��
			if ( fig.type == FIG_LINE )
			{
				if ( i > 0 )
					g.drawLine( pp.x, pp.y, p.x, p.y );
				pp = p;
			}
			
			// Y���ɕ��s�Ȓ����iX=a�̒����j��`��
			if ( fig.type == FIG_Y_LINE )
			{
				g.drawLine( p.x, screen_y0, p.x, screen_y1 );
			}
		}
	}
}
