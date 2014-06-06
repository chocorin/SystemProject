// <applet code="RecognitionApp.class" width="480" height="480"></applet>

import javax.swing.JApplet;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


//
//  �����摜�F���e�X�g �A�v���b�g
//
public class RecognitionApp extends JApplet
{
	// �����摜�F�����W���[��
	protected CharacterRecognizer  recognizer;
	
	// ���p�\�ȓ����ʌv�Z���W���[��
	protected FeatureEvaluater   features[];
	
	// ���p�\��臒l���胂�W���[���i�P�����̓����ʁj
	protected ThresholdDeterminer  thresholds[];
	
	// �O���t�`�惂�W���[��
	protected GraphViewer  graph_viewr;

	// ���s���[�h
	protected int  mode;
	protected final int  RECOGNITION_MODE = 1;
	protected final int  FEATURE_MODE = 2;
	
	// �w�K�E�����F���e�X�g�̌��ʁi��F�����j
	protected float  error, error0, error1;
	
	// �T���v���摜
	protected BufferedImage  sample_images0[];
	protected BufferedImage  sample_images1[];
	
	// �S�T���v���摜�̃C���f�b�N�X�i�摜�����摜�I�u�W�F�N�g �̎Q�Ɓj
	protected TreeMap  image_index;

	// UI�p�R���|�[�l���g	
	protected JPanel  ui_panel;
	protected JLabel  mode_button_label, threshold_label, feature_label[], image_label;
	protected JRadioButton  mode_r_bottun, mode_f_bottun;
	protected JComboBox  threshold_list, feature_list[], image_list;
	protected MainScreen  screen;

	
	// ����������
	public void  init()
	{
		// ���p�\�ȓ����ʌv�Z���W���[����������
		features = new FeatureEvaluater[ 1 ];
		features[ 0 ] = new FeatureLeftLinerity();
	
		// ���p�\��臒l���胂�W���[���i�P�����̓����ʁj��������
		thresholds = new ThresholdDeterminer[ 1 ];
		thresholds[ 0 ] = new ThresholdByAverage();
		
		// �����摜���ʃ��W���[���̐���
		recognizer = new CharacterRecognizer();
		recognizer.setFeatureEvaluater( features[ 0 ] );
		recognizer.setThresholdDeterminer( thresholds[ 0 ] );
		
		// �O���t�`�惂�W���[���̐���
		graph_viewr = new GraphViewer();
		
		// �J�n���̎��s���[�h�̐ݒ�
		mode = FEATURE_MODE;
		
		
		// �S�T���v���摜�̓ǂݍ���
		loadSampleImages();
		
		
		// UI���i�̏�����
		initUIComponents();

		// UI���i�̕\�����X�V
		updateUIComponents();
	}
	
	// �J�n����
	public void  start()
	{
		// �����摜���ʃe�X�g�̎��s
		recognitionTest();
	}
	
	
	//
	//  ���C������
	//
	
	// �T���v���摜���g���������摜�F���̃e�X�g
	public void  recognitionTest()
	{
		// �v����
		// �S�ẴT���v���摜���g���Ċw�K
				recognizer.train( sample_images0, sample_images1 );

				// �S�ẴT���v���摜���g���Č�F�������v�Z
				int  error_count[] = { 0, 0 };
				int  char_no;
				error_count[ 0 ] = 0;
				error_count[ 1 ] = 0;
				for ( int i=0; i<sample_images0.length; i++ )
				{
					char_no = recognizer.recognizeCharacter( sample_images0[ i ] );
					if ( char_no != 0 )
						error_count[ 0 ] ++;
				}
				for ( int i=0; i<sample_images1.length; i++ )
				{
					char_no = recognizer.recognizeCharacter( sample_images1[ i ] );
					if ( char_no != 1 )
						error_count[ 1 ] ++;
				}
				error0 = (float) error_count[ 0 ] / sample_images0.length;
				error1 = (float) error_count[ 1 ] / sample_images1.length;
				error = (float) ( error_count[ 0 ] + error_count[ 1 ] ) / (float) ( sample_images0.length + sample_images1.length );

				// ������ԁE臒l�Ȃǂ��O���t�ɐݒ�
				recognizer.drawGraph( graph_viewr );

				// ��ʂ̍ĕ`��
				repaint();

	}
	
	
	//
	//  ���[�U�C���^�[�t�F�[�X����
	//

	// UI���i�̏�����
	protected void  initUIComponents()
	{	
		// ���[�U�C���^�[�t�F�[�X�p�I�u�W�F�N�g�̍\�z
		Container cp = getContentPane();
		cp.setLayout( new BorderLayout() );
		cp.setBackground( Color.WHITE );
		
		// �C���^�[�t�F�[�X���i�̔z�u���@�̐ݒ�
		ui_panel = new JPanel();
		GridBagLayout  ui_grid = new GridBagLayout();
		GridBagConstraints  ui_grid_label = new GridBagConstraints();
		GridBagConstraints  ui_grid_box = new GridBagConstraints();
		ui_grid_label.gridwidth = 1;
		ui_grid_label.anchor = GridBagConstraints.EAST;
		ui_grid_box.anchor = GridBagConstraints.WEST;
		ui_grid_box.gridwidth = GridBagConstraints.REMAINDER;
		ui_panel.setLayout( ui_grid );
		cp.add( ui_panel, BorderLayout.NORTH );
		
		// ���[�h�I���̂��߂̃��W�I�{�^����ǉ�
		mode_button_label = new JLabel( "���s���[�h�F " );
		mode_r_bottun = new JRadioButton( "�����摜�F��", true );
		mode_f_bottun = new JRadioButton( "�����ʕ\��", false );
		ModeBottunListener  mb_listener = new ModeBottunListener();
		mode_r_bottun.addActionListener( mb_listener );
		mode_f_bottun.addActionListener( mb_listener );
		ui_grid.setConstraints( mode_button_label, ui_grid_label );
		ui_grid.setConstraints( mode_r_bottun, ui_grid_label );
		ui_grid.setConstraints( mode_f_bottun, ui_grid_box );
		ui_panel.add( mode_button_label );
		ui_panel.add( mode_r_bottun );
		ui_panel.add( mode_f_bottun );
		ButtonGroup  mode_group = new ButtonGroup();
		mode_group.add( mode_r_bottun );
		mode_group.add( mode_f_bottun );

		// 臒l�̌v�Z���@�I���̂��߂̃R���{�{�b�N�X�̒ǉ�
		threshold_label = new JLabel( "臒l�̌v�Z���@�F " );
		threshold_list = new JComboBox();
		threshold_list.addActionListener( new ThresholdListListener() );
		ui_grid.setConstraints( threshold_label, ui_grid_label );
		ui_grid.setConstraints( threshold_list, ui_grid_box );
		ui_panel.add( threshold_label );
		ui_panel.add( threshold_list );

		// �R���{�{�b�N�X��臒l�̌v�Z���@�̖��O��ǉ�
		for ( int i=0; i<thresholds.length; i++ )
		{
			if ( thresholds[ i ] != null )
				threshold_list.addItem( (String) thresholds[ i ].getThresholdName() );
		}
		
		// �����ʂ̌v�Z���@�I���̂��߂̃R���{�{�b�N�X�̒ǉ�
		feature_label = new JLabel[ 1 ];
		feature_list = new JComboBox[ 1 ];
		for ( int i=0; i<1; i++ )
		{
			feature_label[ i ] = new JLabel( "������" + (i + 1) + "�̌v�Z���@�F " );
			ui_grid.setConstraints( feature_label[ i ], ui_grid_label );
			ui_panel.add( feature_label[ i ] );
			
			feature_list[ i ] = new JComboBox();
			ui_grid.setConstraints( feature_list[ i ], ui_grid_box );
			ui_panel.add( feature_list[ i ] );
		}

		// �R���{�{�b�N�X�ɓ����ʂ̌v�Z���@�̖��O��ǉ�
		for ( int i=0; i<1; i++ )
		{
			for ( int j=0; j<features.length; j++ )
			{
				if ( features[ j ] != null )
				{
					feature_list[ i ].addItem( (String) features[ j ].getFeatureName() );
					if ( recognizer.getFeatureEvaluater() == features[ j ])
						feature_list[ i ].setSelectedIndex( j );
				}
			}
			feature_list[ i ].addActionListener( new FeatureListListener() );
		}
		
		// �T���v���摜�I���̂��߂̃R���{�{�b�N�X�̒ǉ�
		image_label = new JLabel( "�T���v���摜�F " );
		image_list = new JComboBox();
		ui_grid.setConstraints( image_label, ui_grid_label );
		ui_grid.setConstraints( image_list, ui_grid_box );
		ui_panel.add( image_label );
		ui_panel.add( image_list );
		image_list.setEnabled( true );
		image_list.setEditable( false );
				
		// �R���{�{�b�N�X�ɑS�T���v���摜�̖��O��ǉ�
		if ( image_index != null )
		{
			Set  image_names = image_index.entrySet();
			Iterator  i = image_names.iterator();
			while ( i.hasNext() )
			{
				Map.Entry  entry = (Map.Entry) i.next();
				image_list.addItem( (String) entry.getKey() );
			}
			image_list.addActionListener( new ImageListListener() );
		}
		
		// ���C����ʂ̕`��̈��ǉ�
		screen = new MainScreen();
		cp.add( screen, BorderLayout.CENTER );
		screen.addComponentListener( new MainScreenListener() );
	}
		
	// UI���i�̕\�����X�V
	protected void  updateUIComponents()
	{
		if ( mode == RECOGNITION_MODE )
		{
			mode_r_bottun.setSelected( true );
			mode_f_bottun.setSelected( false );
			threshold_label.setVisible( true );
			threshold_list.setVisible( true );
			image_label.setVisible( false );
			image_list.setVisible( false );
		}
		else
		{
			mode_r_bottun.setSelected( false );
			mode_f_bottun.setSelected( true );
			threshold_list.setVisible( false );
			threshold_label.setVisible( false );
			image_label.setVisible( true );
			image_list.setVisible( true );
		}
	}
	
	// ���s���[�h�I�������̂��߂̃��X�i�N���X�i�����N���X�j
	class  ModeBottunListener implements ActionListener
	{
		// �A�C�e�����I�����ꂽ���ɌĂ΂�鏈��
		public void  actionPerformed( ActionEvent e )
		{
			// �I�����ꂽ�{�^�����擾
			JRadioButton  selected = (JRadioButton) e.getSource();
			
			// �I�����ꂽ�{�^���ɉ����Ď��s���[�h�̕ύX
			if ( selected == mode_r_bottun )
				mode = RECOGNITION_MODE;
			else if ( selected == mode_f_bottun )
				mode = FEATURE_MODE;
			
			// ���s���[�h�ɉ����ăR���|�[�l���g�̕\����ύX
			updateUIComponents();
			
			// �����摜���ʃe�X�g���ēx���s
			if ( mode == RECOGNITION_MODE )
				recognitionTest();
			
			// �I���摜�̓����ʂ��v�Z
			if ( mode == FEATURE_MODE )
			{	
				// �I�����ꂽ�摜���擾
				BufferedImage  selected_image = null;
				String  selected_image_name = (String) image_list.getSelectedItem();
				if ( ( selected_image_name != null ) && ( image_index != null ) )
					selected_image = (BufferedImage) image_index.get( selected_image_name );
			
				// ���݂̓����ʌv�Z���W���[�����g���ĉ摜�̓����ʂ��v�Z
				if ( selected_image != null )
					recognizer.getFeatureEvaluater().evaluate( selected_image );			
			}
			
			// �S�̂��ĕ`��
			repaint();
		}
	}
	
	// 臒l�̌v�Z���@�̑I�������̂��߂̃��X�i�N���X�i�����N���X�j
	class  ThresholdListListener implements ActionListener
	{
		// �A�C�e�����I�����ꂽ���ɌĂ΂�鏈��
		public void  actionPerformed( ActionEvent e )
		{
			// �I�����ꂽ臒l�̌v�Z���@�̃C���f�b�N�X���擾
			int  no = threshold_list.getSelectedIndex();
			
			// �I�����ꂽ�C���f�b�N�X�������ł���Ή��������I��
			if ( ( no == -1 ) || ( thresholds[ no ] == null ) )
				return;
				
			// �I�����ꂽ臒l�̌v�Z���@�����݂̂��̂Ɠ����ŗL��Ή��������I��
			if ( thresholds[ no ] == recognizer.getThresholdDeterminer() )
				return;
				
			// �I�����ꂽ臒l�̌v�Z���@��ݒ�
			recognizer.setThresholdDeterminer( thresholds[ no ] );
			
			// �����摜���ʃe�X�g���ēx���s
			recognitionTest();
			
			// �S�̂��ĕ`��
			repaint();
		}
	}
	
	// �����ʂ̌v�Z���@�̑I�������̂��߂̃��X�i�N���X�i�����N���X�j
	class  FeatureListListener implements ActionListener
	{
		// �A�C�e�����I�����ꂽ���ɌĂ΂�鏈��
		public void  actionPerformed( ActionEvent e )
		{
			// �I�����ꂽ�����ʂ̌v�Z���@�̃C���f�b�N�X���擾
			int  no = feature_list[ 0 ].getSelectedIndex();
			
			// �I�����ꂽ�C���f�b�N�X�������Ȃ牽�������I��
			if ( ( no == -1 ) || ( features[ no ] == null ) )
				return;
			// �I�����ꂽ臒l�̌v�Z���@�����݂̂��̂Ɠ����Ȃ牽�������I��
			if ( features[ no ] == recognizer.getFeatureEvaluater() )
				return;
							
			// �I�����ꂽ臒l�̌v�Z���@��ݒ�
			recognizer.setFeatureEvaluater( features[ no ] );

			// �����摜���ʃe�X�g���ēx���s
			recognitionTest();
			
			// �S�̂��ĕ`��
			repaint();
		}
	}
	
	// �\���摜�̑I�������̂��߂̃��X�i�N���X�i�����N���X�j
	class  ImageListListener implements ActionListener
	{
		// �A�C�e�����I�����ꂽ���ɌĂ΂�鏈��
		public void  actionPerformed( ActionEvent e )
		{
			// �I�����ꂽ�摜���擾
			String  selected_image_name = (String) image_list.getSelectedItem();
			BufferedImage  selected_image = (BufferedImage) image_index.get( selected_image_name );
			
			// ���݂̓����ʌv�Z���W���[�����g���ĉ摜�̓����ʂ��v�Z 
			recognizer.getFeatureEvaluater().evaluate( selected_image );
			
			// �S�̂��ĕ`��
			repaint();
		}
	}
	
	// ���C����ʂ̃T�C�Y�ύX�����m���邽�߂̃��X�i�N���X�i�����N���X�j
	class  MainScreenListener implements ComponentListener
	{
		// ��ʏ㕔�̗]���i�F�����\���̂��߂̃X�y�[�X�j
		final public int  top_margin = 72;
		
		// �T�C�Y�ύX���ꂽ���ɌĂ΂�鏈��
		public void componentResized( ComponentEvent e )
		{
			// �O���t�̕`��͈͂�ݒ�
			graph_viewr.setDrawArea( 0, top_margin, screen.getWidth(), screen.getHeight() );
			
			// �S�̂��ĕ`��
			repaint();
		}
		public void componentMoved( ComponentEvent e )
		{
		}
		public void componentShown( ComponentEvent e )
		{
		}
		public void componentHidden( ComponentEvent e )
		{
		}
	}
	
	// ���C����ʕ`��̂��߂̃R���|�[�l���g�N���X�i�����N���X�j
	class  MainScreen extends JComponent
	{
		// �`�揈��
		public void  paint( Graphics g )
		{
			// �e�R���|�[�l���g�̕`��
			super.paint( g );
			
			// �����摜�F�����[�h�ł͓�����ԁE�F������`��
			if ( mode == RECOGNITION_MODE )
			{
				// ������Ԃ�\���O���t��`��
				graph_viewr.paint( g );
				
				// ��F������\��
				String  message;
				g.setColor( Color.BLACK );
				message = "��F����: " + error;
				g.drawString( message, 16, 16 );
				message = "8 �̌�F����: " + error0;
				g.drawString( message, 16, 32 );
				message = "B �̌�F����: " + error1;
				g.drawString( message, 16, 48 );
				if ( mode == RECOGNITION_MODE )
				{
					message = "臒l: " + recognizer.getThresholdDeterminer().getThreshold();
					g.drawString( message, 16, 64 );
				}
			}
			// �����ʕ\�����[�h�ł͑I���摜�̓����ʌv�Z���ʂ�`��
			else if ( mode == FEATURE_MODE )
			{
				// �I���摜�̓����ʂ̌v�Z���ʂ�`��
				recognizer.getFeatureEvaluater().paintImageFeature( g );
			}		
		}
	}
	
	
	//
	//  �T���v���摜�̓ǂݍ��݂̂��߂̓������\�b�h
	//
	
	// �T���v���摜�̓ǂݍ���
	public void  loadSampleImages()
	{
		// 8 �̃T���v���摜��ǂݍ��݁iSamples8Bgif/pic8_001.gif �` pic8_105.gif ��105���j
				sample_images0 = loadBufferedImages( "Samples8Bgif/pic8_", 1, 105, 3, ".gif" );
				
				// B �̃T���v���摜��ǂݍ��݁iSamples8Bgif/picB_001.gif �` picB_105.gif ��105���j
				sample_images1 = loadBufferedImages( "Samples8Bgif/picB_", 1, 105, 3, ".gif" );

				// �S�Ẳ摜���C���f�b�N�X�ɋL�^����
				image_index = new TreeMap();
				String  name;
				for ( int i=0; i<sample_images0.length; i++ )
				{
					name = "" + (i + 1);
					while ( name.length() < 3 )
						name = "0" + name;
					name = "pict8_" + name;
					image_index.put( name, sample_images0[ i ] );
				}
				for ( int i=0; i<sample_images1.length; i++ )
				{
					name = "" + (i + 1);
					while ( name.length() < 3 )
						name = "0" + name;
					name = "pictB_" + name;
					image_index.put( name, sample_images1[ i ] );
				}
			}
			
			// �A�ԉ摜�̔z��ւ̓ǂݍ���
			protected BufferedImage[]  loadBufferedImages( String prefix, int count_start, int count_end, int count_width, String suffix )
			{
				// �ǂݍ��񂾉摜���i�[����z��̃T�C�Y��������
				BufferedImage[]  images = new BufferedImage[ count_end - count_start + 1 ];
				
				// �A�ԉ摜�����ɓǂݍ���
				for ( int i=count_start; i<=count_end; i++ )
				{
					// �t�@�C�������쐬
					String  filename = "" + i;
					while ( filename.length() < count_width )
						filename = "0" + filename;
					filename = prefix + filename + suffix;
					
					// �摜��ǂݍ���
					images[ i - count_start ] = getBufferedImage( filename );
				}
				
				return  images;
			}
			
			// Image I/O ���g�����摜�̓ǂݍ���
			protected BufferedImage getBufferedImage( String filename )
			{
				try
				{
					java.io.File  file = new java.io.File( filename );
					BufferedImage image = javax.imageio.ImageIO.read( file );
					return  image;
				}
				catch ( Exception e )
				{
					return  null;
				}
			}
// �v����
			


	//
	//  ���C���֐�
	//
	public static void  main( String[] args )
	{
		// �A�v���b�g���N��
		Frame  frame = new Frame( "Recognition App" );
		frame.addWindowListener( new WindowAdapter()
			{
				public void windowClosing( WindowEvent evt )
				{
					System.exit(0);
				}
			} );
		RecognitionApp  applet = new RecognitionApp();
		applet.init();
		frame.add( applet );
		frame.setSize( 480, 480 );
		frame.show();
		applet.start();
	}
}