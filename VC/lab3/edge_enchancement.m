function [ output_image ] = edge_enchancement( input_image, input_alpha )
%   Edge enchancement function
%
%   input_image = the image which the edge enchancement will be applied
%   input_alpha = the ratio used to contrast the edges (0.2-0.5 is alright)

%output_image = im2uint8(edge(input_image,'Canny'));
%output_image = input_image - output_image * input_alpha;

%kernel = -1*ones(3);
%kernel(2,2) = 8;
%output_image = imfilter(input_image, kernel);

h = fspecial('laplacian',input_alpha);
output_image = input_image - imfilter(input_image, h);

%output_image = imsharpen(input_image);
end

