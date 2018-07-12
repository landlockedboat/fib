#version 330 core

layout (location = 0) in vec3 vertex;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec3 color;
layout (location = 3) in vec2 texCoord;

out vec4 frontColor;
out vec2 vtexCoord;

uniform mat4 modelViewProjectionMatrix;
uniform mat3 normalMatrix;

uniform float amplitude=.1;
uniform float freq=1.0;
uniform float time;

void main()
{
    vec3 N = normalize(normalMatrix * normal);
    frontColor = vec4(1.0) * N.z;
    vtexCoord = texCoord;
	vec3 aVertex = vertex + distance(time, freq, amplitude) * N;
    gl_Position = modelViewProjectionMatrix * vec4(vertex, 1.0);
}

float distance(float time, float freq, float amplitude){
	return clamp(time * freq, -amplitude, amplitude);
}
