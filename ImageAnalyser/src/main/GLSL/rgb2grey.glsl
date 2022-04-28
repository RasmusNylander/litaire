#version 120

precision mediump float;

uniform sampler2D u_image;
varying vec2 v_texcoord;

void main(void) {
    vec4 color = texture2D(u_image, v_texcoord);
    float grey = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    gl_FragColor = vec4(grey, grey, grey, color.a);
}