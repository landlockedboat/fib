function [outputValue] = smoothPixel(image,offset,ratio,row,col)
% calculates the smoothed value of a certain pixel
outputValue = 0;
for i = (row-offset):(row+offset)
   for j = (col-offset):(col+offset)
      outputValue = outputValue + image(i,j);
   end
end
outputValue = outputValue / (ratio*ratio);
end

