function [out] = lab3_ex2(in)
% non linear filter for salt & pepper noise
% use colfilt and check if the pixel is black or white before filtering
  
  I = imread('marti.jpg');
  Isp = imnoise(I, 'salt & pepper', 0.05);
  Iusp = unsalpepper(Isp);
  
  imshow(Iusp);
end

