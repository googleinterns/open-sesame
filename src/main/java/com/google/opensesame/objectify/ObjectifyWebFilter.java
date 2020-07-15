package com.google.opensesame.objectify;

import com.googlecode.objectify.ObjectifyFilter;
import javax.servlet.annotation.WebFilter;

@WebFilter(urlPatterns = {"/*"})
public class ObjectifyWebFilter extends ObjectifyFilter {}
