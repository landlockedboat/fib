function [ output_image ] = blurring( input_image, input_angle )
%   Directional blur
%
%   input_image = the image which the blur will be applied
%   pixel_displacement = the number of pixels the image will be moved
%   input_angle = the direction which the pixels will be moved
pixel_displacement = 15;
h = zeros(pixel_displacement);
h((pixel_displacement+1)/2,1:(pixel_displacement+1)/2) = 1;
h = h/(pixel_displacement/2); % Normalise image
h = imrotate(h,input_angle); % Rotate image

output_image = imfilter(input_image,h);
end

