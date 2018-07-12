function[SNR] = lab2_ex3(I)
% calculate noise on image rescale

newImage = imresize(I, 3/7);
newImage = imresize(newImage, size(I));

%SNR  =  10 log10(Ps/PN)
Ps = std(std(double(I - newImage)));
PN = mean(mean(I));

SNR = 10*log10(Ps/PN);
end