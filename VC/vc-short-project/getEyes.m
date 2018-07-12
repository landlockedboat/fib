function [ E ] = getEyes( I, eyesPos, dist )

off = dist / 2;

xmin = eyesPos(1) - off;
ymin = eyesPos(2) - off;

rect = [xmin ymin dist dist];

E = imcrop(I, rect);

end
