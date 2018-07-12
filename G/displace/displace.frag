#version 330 core

in vec4 frontColor;
out vec4 fragColor;

in vec3 eye;

void main()
{
	vec3 dx = dFdx(eye);
	vec3 dy = dFdy(eye);
	vec3 N = normalize(cross(dx, dy));
    fragColor = vec4(N.z);
}
