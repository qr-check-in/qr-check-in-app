//package com.example.qrcheckin;
//
//import android.graphics.Bitmap;
//import android.widget.ImageView;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static org.junit.Assert.assertNotNull;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.example.qrcheckin.Event.CreateGenerateEventQR;
//
//public class QrCodeTest {
//
//    @Mock
//    ImageView ivCheckInQR;
//
//    @Mock
//    private CreateGenerateEventQR createGenerateEventQR;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        createGenerateEventQR = mock(CreateGenerateEventQR.class);
//
//    }
//
//    @Test
//    public void testSetQRCode() {
//        String unhashedContent = "Test Content";
//        Bitmap bitmap = null;
//
//        // Mocked bitmap
//        bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
//        // Mock the generateQRCode method
//        when(createGenerateEventQR.generateQRCode(unhashedContent)).thenReturn(bitmap);
//
//        createGenerateEventQR.setQRCode(ivCheckInQR, unhashedContent, false);
//
//        // Verify that the ImageView is updated with the correct bitmap
//        verify(ivCheckInQR).setImageBitmap(bitmap);
//    }
//
//    @Test
//    public void testGenerateQRCode() {
//        String unhashedContent = "Test Content";
//        Bitmap bitmap = createGenerateEventQR.generateQRCode(unhashedContent);
//        assertNotNull(bitmap);
//    }
////
////    @Test
////    public void testSaveBitmapImage() {
////        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
////        String filename = "test_image.jpg";
////        // Mock the expected URI
////        when(createGenerateEventQR.saveBitmapImage(bitmap, filename)).thenReturn("file://test/image/path");
////
////        String uri = createGenerateEventQR.saveBitmapImage(bitmap, filename);
////
////        // Verify that the image is saved to the expected location
////        assertNotNull(uri);
////    }
////
////    @Test
////    public void testShareQRCode() {
////        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
////        String eventName = "Test Event";
////        String eventDate = "2024-04-08";
////
////        createGenerateEventQR.shareQRCode(new QRCode("uri", null, null), bitmap);
////
////        // Verify that the correct extras are added to the Intent
////        // Example: verify(intent).putExtra("QRCodeBitmap", bitmap);
////    }
//}
